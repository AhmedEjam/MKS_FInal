package com.ahmedyejam.mks.data.network

import android.net.Uri
import com.ahmedyejam.mks.util.copyToWithLimit
import com.ahmedyejam.mks.util.MksLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class RemoteAssetFetcher(
    private val clientFactory: (RemoteAssetPolicy) -> OkHttpClient = { policy ->
        OkHttpClient.Builder()
            .connectTimeout(policy.connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(policy.readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(policy.writeTimeoutSeconds, TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }
) {
    companion object {
        private val BLOCKED_HOSTS = setOf("localhost", "127.0.0.1", "::1", "0.0.0.0")
        private const val MAX_REDIRECTS = 5

        /** Returns true if the given [InetAddress] is a private, loopback, link-local, or metadata IP. */
        private fun isBlockedAddress(addr: InetAddress): Boolean {
            return addr.isLoopbackAddress ||
                   addr.isSiteLocalAddress ||
                   addr.isLinkLocalAddress ||
                   addr.isAnyLocalAddress ||
                   // Cloud metadata endpoint (AWS, GCP, Azure)
                   addr.hostAddress == "169.254.169.254"
        }

        /** Validates a URL's scheme, host, and DNS resolution against SSRF rules. */
        private fun validateUrl(url: HttpUrl, policy: RemoteAssetPolicy): String? {
            val scheme = url.scheme.lowercase()
            if (scheme !in listOf("http", "https")) {
                return "Only HTTP(S) image URLs are supported."
            }
            if (scheme == "http" && !policy.allowPlainHttp) {
                return "Blocked: plain HTTP redirect requires consent."
            }
            val host = url.host.lowercase()
            if (host.isBlank() || host in BLOCKED_HOSTS) {
                return "Blocked: cannot fetch from local or reserved hosts."
            }
            return try {
                val resolved = InetAddress.getAllByName(host)
                if (resolved.any { isBlockedAddress(it) }) {
                    "Blocked: URL resolves to a private or reserved IP address."
                } else {
                    null
                }
            } catch (e: Exception) {
                "Cannot resolve host: $host"
            }
        }
    }

    suspend fun fetch(url: String, policy: RemoteAssetPolicy = RemoteAssetPolicy.Default): RemoteAssetResult = withContext(Dispatchers.IO) {
        val initialUrl = runCatching { url.toHttpUrlOrNull() }.getOrNull()
            ?: return@withContext RemoteAssetResult(error = "Invalid remote image URL.")

        // Validate the initial URL
        val initialError = validateUrl(initialUrl, policy)
        if (initialError != null) {
            return@withContext RemoteAssetResult(error = initialError)
        }
        if (initialUrl.scheme == "http" && !policy.allowPlainHttp) {
            return@withContext RemoteAssetResult(
                warning = "Plain HTTP image URL was not downloaded. Confirm insecure image downloads to import it.",
                plainHttpConsentRequired = true
            )
        }

        var currentUrl = initialUrl
        val client = clientFactory(policy)

        try {
            repeat(MAX_REDIRECTS + 1) { redirectCount ->
                val request = Request.Builder().url(currentUrl).build()
                client.newCall(request).execute().use { response ->
                    if (response.isRedirect) {
                        if (redirectCount == MAX_REDIRECTS) {
                            return@withContext RemoteAssetResult(error = "Too many redirects.")
                        }
                        val location = response.header("Location")
                            ?: return@withContext RemoteAssetResult(error = "Redirect has no destination.")
                        val resolvedUrl = currentUrl.resolve(location)
                            ?: return@withContext RemoteAssetResult(error = "Invalid redirect destination.")
                        // Re-validate every redirect hop
                        val redirectError = validateUrl(resolvedUrl, policy)
                        if (redirectError != null) {
                            return@withContext RemoteAssetResult(error = redirectError)
                        }
                        currentUrl = resolvedUrl
                        return@repeat
                    }

                    if (!response.isSuccessful) {
                        return@withContext RemoteAssetResult(error = "Download failed with HTTP ${response.code}.")
                    }
                    val body = response.body ?: return@withContext RemoteAssetResult(error = "Remote image response body is empty.")
                    val contentType = body.contentType()?.toString()
                    if (policy.requireImageContentType && contentType != null && !contentType.startsWith("image/", ignoreCase = true)) {
                        return@withContext RemoteAssetResult(error = "Remote asset is not an image ($contentType).")
                    }
                    val contentLength = body.contentLength()
                    if (contentLength > policy.maxBytes) {
                        return@withContext RemoteAssetResult(error = "Remote image exceeds ${policy.maxBytes / (1024 * 1024)} MB limit.")
                    }
                    val output = ByteArrayOutputStream()
                    body.byteStream().use { input -> input.copyToWithLimit(output, policy.maxBytes) }
                    return@withContext RemoteAssetResult(
                        bytes = output.toByteArray(),
                        warning = if (currentUrl.scheme == "http") "Downloaded image over plain HTTP after user confirmation." else null,
                        contentType = contentType
                    )
                }
            }
            RemoteAssetResult(error = "Download failed.")
        } catch (e: Exception) {
            MksLogger.w("RemoteAssetFetcher", "Remote asset download failed", e)
            RemoteAssetResult(error = e.message ?: "Failed to download remote image.")
        }
    }
}
