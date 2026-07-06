package com.ahmedyejam.mks.data.network

import android.util.Log
import com.ahmedyejam.mks.data.model.AiProviderConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * HTTP error from an OpenAI-compatible endpoint.
 *
 * @param status HTTP status code
 * @param body   Partial response body (first 300 chars) for diagnostics
 */
class AiHttpError(val status: Int, body: String) : Exception("AI HTTP $status: ${body.take(300)}") {
    /** True for client-side errors that will never succeed on retry. */
    val isNonRetriable: Boolean get() = status in setOf(400, 401, 403, 404, 422)
}

private const val TAG = "AiClient"
private const val DEFAULT_RETRIES = 3
private const val BACKOFF_BASE_MS = 1_000L

/**
 * Unified OpenAI-compatible `/chat/completions` HTTP client.
 *
 * This is the Kotlin equivalent of `aiClient.js` from the DocQuiz AI desktop project.
 * It targets any provider that speaks the OpenAI chat API (Groq, Gemini, DeepSeek,
 * Ollama with `/v1`, etc.) and is **completely separate** from [OllamaRepository],
 * which uses Ollama's native `/api/generate` streaming protocol.
 *
 * ### Features
 * - Exponential-backoff retry (3 attempts by default)
 * - `isNonRetriable` fast-fail for 400/401/403/404/422
 * - Optional Bearer API key injection
 * - Two call modes:
 *   - [chatComplete] — plain text system + user messages
 *   - [generateWithImage] — vision model with base64-encoded image(s)
 */
@Singleton
class AiClient @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)   // Large models can take 2–3 min
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // ──────────────────────────────────────────────────────────────────────────
    // Public API
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Lists available models from the provider.
     * Ollama natively supports `/api/tags` via OllamaRepository, but this targets
     * OpenAI-compatible `/v1/models` endpoints.
     */
    suspend fun listModels(baseUrl: String, apiKey: String? = null): List<String> = withContext(Dispatchers.IO) {
        val base = baseUrl.trimEnd('/')
        val url = if (base.endsWith("/v1")) "$base/models" else "$base/v1/models"
        val requestBuilder = Request.Builder().url(url).get()
        
        if (!apiKey.isNullOrBlank()) {
            val headerValue = if (apiKey.contains(' ')) apiKey else "Bearer $apiKey"
            requestBuilder.header("Authorization", headerValue)
        }

        val request = requestBuilder.build()
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string()
                    throw AiHttpError(response.code, errorBody ?: "No error body")
                }
                
                val bodyString = response.body?.string() ?: throw IOException("Empty response body")
                val json = JSONObject(bodyString)
                val dataArray = json.optJSONArray("data") ?: return@withContext emptyList<String>()
                
                val result = mutableListOf<String>()
                for (i in 0 until dataArray.length()) {
                    val id = dataArray.optJSONObject(i)?.optString("id")
                    if (!id.isNullOrBlank()) result.add(id)
                }
                result
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to list models", e)
            throw e
        }
    }

    /**
     * Chat completion — plain text system + user messages.
     *
     * @param config      Provider/model configuration.
     * @param systemPrompt System-role instruction.
     * @param userMessage  User-role content.
     * @param temperature  Sampling temperature (default 0.1 for deterministic extraction).
     * @param maxTokens    Max tokens in the response (default 8192).
     * @param jsonMode     Request JSON-structured output where the provider supports it.
     * @return The model's text response.
     */
    suspend fun chatComplete(
        config: AiProviderConfig,
        systemPrompt: String,
        userMessage: String,
        temperature: Double = 0.1,
        maxTokens: Int = 8192,
        jsonMode: Boolean = false,
    ): String = withContext(Dispatchers.IO) {
        val payload = JSONObject().apply {
            put("model", config.model)
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", systemPrompt)
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userMessage)
                })
            })
            put("temperature", temperature)
            put("max_tokens", maxTokens)

            // Provider-specific JSON mode handling
            if (jsonMode) {
                when (config.providerId) {
                    "ollama", "ollama-server" -> put("format", "json")
                    else -> put("response_format", JSONObject().put("type", "json_object"))
                }
            }
            
            // Force maximum context window for Ollama
            if (config.providerId.startsWith("ollama")) {
                put("options", JSONObject().put("num_ctx", 32768))
            }
        }

        val response = fetchWithRetry(config, payload)
        response
            .optJSONArray("choices")
            ?.optJSONObject(0)
            ?.optJSONObject("message")
            ?.optString("content")
            ?: ""
    }

    /**
     * Vision completion — send base64-encoded image(s) with a system prompt.
     * Images go in the user turn; the OCR/analysis instruction goes in the system turn.
     *
     * @param config  Provider/model configuration (must use a vision-capable model).
     * @param prompt  System instruction (e.g. the OCR system prompt).
     * @param images  List of base64-encoded PNG/JPEG strings (no data-URI prefix needed).
     * @return The model's text response.
     */
    suspend fun generateWithImage(
        config: AiProviderConfig,
        prompt: String,
        images: List<String>,
    ): String = withContext(Dispatchers.IO) {
        val imageContent = JSONArray().apply {
            images.forEach { b64 ->
                put(JSONObject().apply {
                    put("type", "image_url")
                    put("image_url", JSONObject().apply {
                        put("url", "data:image/png;base64,$b64")
                        put("detail", "high")   // High-res for OCR accuracy on dense pages
                    })
                })
            }
        }

        val payload = JSONObject().apply {
            put("model", config.model)
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", prompt)
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", imageContent)
                })
            })
            put("temperature", 0)        // Deterministic — OCR should be faithful, not creative
            put("max_tokens", 8192)      // Dense pages can produce 3000–5000 tokens of text
            
            // Force maximum context window for Ollama
            if (config.providerId.startsWith("ollama")) {
                put("options", JSONObject().put("num_ctx", 32768))
            }
        }

        val response = fetchWithRetry(config, payload)
        response
            .optJSONArray("choices")
            ?.optJSONObject(0)
            ?.optJSONObject("message")
            ?.optString("content")
            ?: ""
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Internal
    // ──────────────────────────────────────────────────────────────────────────

    private fun fetchWithRetry(config: AiProviderConfig, payload: JSONObject): JSONObject {
        val endpoint = "${config.resolvedBaseUrl}/chat/completions"
        var lastError: Exception? = null

        for (attempt in 0..DEFAULT_RETRIES) {
            try {
                val body = payload.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url(endpoint)
                    .applyAuth(config.apiKey)
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        val bodyText = response.body?.string() ?: ""
                        throw AiHttpError(response.code, bodyText)
                    }
                    val json = JSONObject(response.body?.string() ?: "{}")

                    // Log token usage for diagnostics
                    json.optJSONObject("usage")?.let { usage ->
                        Log.d(TAG, "[${config.model}] prompt=${usage.optInt("prompt_tokens")}, " +
                                "completion=${usage.optInt("completion_tokens")}")
                    }

                    return json
                }
            } catch (e: AiHttpError) {
                if (e.isNonRetriable) throw e   // Don't retry 401/403/404/422
                lastError = e
            } catch (e: SocketTimeoutException) {
                lastError = AiHttpError(408, "Timeout: ${e.message}")
            } catch (e: Exception) {
                lastError = e
            }

            if (attempt < DEFAULT_RETRIES) {
                val backoffMs = BACKOFF_BASE_MS * (1L shl attempt)   // 1s, 2s, 4s
                Log.w(TAG, "Attempt ${attempt + 1} failed, retrying in ${backoffMs}ms: ${lastError?.message}")
                Thread.sleep(backoffMs)
            }
        }

        throw lastError ?: AiHttpError(0, "Unknown error")
    }

    private fun Request.Builder.applyAuth(apiKey: String?): Request.Builder {
        if (!apiKey.isNullOrBlank()) {
            val value = if (apiKey.contains(' ')) apiKey else "Bearer $apiKey"
            header("Authorization", value)
        }
        return this
    }
}
