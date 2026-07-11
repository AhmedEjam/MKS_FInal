package com.ahmedyejam.mks.service

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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
    // Defaults instantly available offline
    private val _isGlobalSearchEnabled = MutableStateFlow(true)
    val isGlobalSearchEnabled: StateFlow<Boolean> = _isGlobalSearchEnabled.asStateFlow()

    suspend fun fetchAndActivate() {
        withContext(Dispatchers.IO) {
            try {
                // Strict 2000ms timeout enforced
                withTimeoutOrNull(2000L) {
                    remoteConfig.fetchAndActivate().await()
                    _isGlobalSearchEnabled.update {
                        remoteConfig.getBoolean("feature_global_search_enabled")
                    }
                }
            } catch (e: Exception) {
                // Fails silently, StateFlow retains local defaults
            }
        }
    }
}
