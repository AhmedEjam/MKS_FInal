package com.ahmedyejam.mks.service

import com.ahmedyejam.mks.util.MksLogger
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    companion object {
        private const val TAG = "RemoteConfigManager"

        /**
         * Hard ceiling on the cold-start config fetch.
         *
         * Remote config must never delay first paint, so the fetch is abandoned rather than
         * awaited; the StateFlow defaults below already carry usable values.
         */
        private const val FETCH_TIMEOUT_MS = 2_000L

        private const val KEY_GLOBAL_SEARCH_ENABLED = "feature_global_search_enabled"
    }

    // Defaults instantly available offline
    private val _isGlobalSearchEnabled = MutableStateFlow(true)
    val isGlobalSearchEnabled: StateFlow<Boolean> = _isGlobalSearchEnabled.asStateFlow()

    suspend fun fetchAndActivate() {
        withContext(Dispatchers.IO) {
            try {
                withTimeoutOrNull(FETCH_TIMEOUT_MS) {
                    remoteConfig.fetchAndActivate().await()
                    _isGlobalSearchEnabled.update {
                        remoteConfig.getBoolean(KEY_GLOBAL_SEARCH_ENABLED)
                    }
                }
            } catch (e: IOException) {
                // Offline or transient network failure: the local defaults stand. Logged rather
                // than swallowed so a config that never arrives is diagnosable instead of invisible.
                MksLogger.w(TAG, "Remote config fetch failed; keeping local defaults", e)
            } catch (e: FirebaseRemoteConfigException) {
                MksLogger.w(TAG, "Remote config rejected the fetch; keeping local defaults", e)
            }
        }
    }
}
