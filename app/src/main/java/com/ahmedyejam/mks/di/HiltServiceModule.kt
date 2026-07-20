package com.ahmedyejam.mks.di

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltServiceModule {

    /** Serve cached config for an hour before hitting the network again. */
    private const val MIN_FETCH_INTERVAL_SECONDS = 3600L

    /** Matches `RemoteConfigManager.FETCH_TIMEOUT_MS`; config must not delay first paint. */
    private const val FETCH_TIMEOUT_SECONDS = 2L

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = MIN_FETCH_INTERVAL_SECONDS
                fetchTimeoutInSeconds = FETCH_TIMEOUT_SECONDS
            },
        )
    }
}
