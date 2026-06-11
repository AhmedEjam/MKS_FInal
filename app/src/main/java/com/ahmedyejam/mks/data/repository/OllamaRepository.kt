package com.ahmedyejam.mks.data.repository

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import okio.IOException

@JsonClass(generateAdapter = true)
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val system: String? = null,
    val stream: Boolean = false
)

@JsonClass(generateAdapter = true)
data class OllamaResponse(
    val model: String,
    val created_at: String,
    val response: String,
    val done: Boolean
)

class OllamaRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS) // LLMs can take time to respond
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val requestAdapter = moshi.adapter(OllamaRequest::class.java)
    private val responseAdapter = moshi.adapter(OllamaResponse::class.java)

    suspend fun generateCompletion(
        baseUrl: String,
        modelName: String,
        prompt: String,
        systemPrompt: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = baseUrl.trimEnd('/') + "/api/generate"
            val requestObj = OllamaRequest(model = modelName, prompt = prompt, system = systemPrompt, stream = false)
            val jsonPayload = requestAdapter.toJson(requestObj)
            
            val body = jsonPayload.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
                
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(IOException("HTTP Error: ${response.code}"))
                }
                
                val responseBodyStr = response.body?.string()
                if (responseBodyStr.isNullOrBlank()) {
                    return@withContext Result.failure(IOException("Empty response body"))
                }
                
                val ollamaResponse = responseAdapter.fromJson(responseBodyStr)
                if (ollamaResponse != null) {
                    Result.success(ollamaResponse.response)
                } else {
                    Result.failure(IOException("Failed to parse response"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun generateCompletionStream(
        baseUrl: String,
        modelName: String,
        prompt: String,
        systemPrompt: String? = null
    ): Flow<String> = flow {
        val url = baseUrl.trimEnd('/') + "/api/generate"
        val requestObj = OllamaRequest(model = modelName, prompt = prompt, system = systemPrompt, stream = true)
        val jsonPayload = requestAdapter.toJson(requestObj)
        
        val body = jsonPayload.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
            
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("HTTP Error: ${response.code} - ${response.message}")
            }
            
            val bodyStream = response.body?.byteStream() ?: throw IOException("Empty response body")
            val reader = BufferedReader(InputStreamReader(bodyStream))
            
            reader.useLines { lines ->
                for (line in lines) {
                    if (line.isNotBlank()) {
                        try {
                            val ollamaResponse = responseAdapter.fromJson(line)
                            if (ollamaResponse != null) {
                                emit(ollamaResponse.response)
                            }
                        } catch (e: Exception) {
                            // Ignore parsing errors for incomplete stream chunks if any
                        }
                    }
                }
            }
        }
    }
}
