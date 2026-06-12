package com.ahmedyejam.mks.data.repository

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.currentCoroutineContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import android.util.Log

class OllamaApiException(message: String) : Exception(message)

@JsonClass(generateAdapter = true)
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val system: String? = null,
    val stream: Boolean = false,
    val options: Map<String, Any>? = null
)

@JsonClass(generateAdapter = true)
data class OllamaResponse(
    val model: String,
    @Json(name = "created_at") val createdAt: String,
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

    fun generateCompletionStream(
        baseUrl: String,
        modelName: String,
        prompt: String,
        systemPrompt: String? = null,
        options: Map<String, Any>? = null
    ): Flow<String> = flow {
        val formattedBaseUrl =
            if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
                "http://$baseUrl"
            } else {
                baseUrl
            }
        val url = formattedBaseUrl.trimEnd('/') + "/api/generate"
        val requestObj = OllamaRequest(
            model = modelName, 
            prompt = prompt, 
            system = systemPrompt, 
            stream = true,
            options = options
        )
        val jsonPayload = requestAdapter.toJson(requestObj)
        
        val body = jsonPayload.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
            
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string()
                    if (errorBody != null && errorBody.contains("\"error\"")) {
                        try {
                            val json = JSONObject(errorBody)
                            val errMsg = json.optString("error")
                            if (errMsg.isNotBlank()) {
                                throw OllamaApiException(errMsg)
                            }
                        } catch (_: Exception) {}
                    }
                    throw IOException("HTTP Error: ${response.code} - ${response.message}\n$errorBody")
                }
                
                val bodyStream = response.body?.byteStream() ?: throw IOException("Empty response body")
                val reader = BufferedReader(InputStreamReader(bodyStream))
                
                reader.useLines { lines ->
                    for (line in lines) {
                        currentCoroutineContext().ensureActive()
                        
                        if (line.isNotBlank()) {
                            if (line.contains("\"error\"")) {
                                try {
                                    val json = JSONObject(line)
                                    val errMsg = json.optString("error")
                                    if (errMsg.isNotBlank()) {
                                        throw OllamaApiException(errMsg)
                                    }
                                } catch (_: Exception) {}
                            }
                            
                            try {
                                val ollamaResponse = responseAdapter.fromJson(line)
                                if (ollamaResponse != null) {
                                    emit(ollamaResponse.response)
                                }
                            } catch (e: Exception) {
                                if (e is CancellationException) throw e
                                Log.e("OllamaRepository", "Failed to parse stream chunk: $line", e)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            if (e is SocketTimeoutException) {
                throw OllamaApiException("Connection timed out. The AI model is taking too long to respond.")
            }
            throw e
        }
    }.flowOn(Dispatchers.IO)
}
