package com.ahmedyejam.mks

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.ahmedyejam.mks.di.AppModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MksApplication : Application(), ImageLoaderFactory {
    lateinit var appModule: AppModule

    companion object {
        private const val MEMORY_CACHE_PERCENT = 0.25
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    // Set memory cache to 25% of available RAM for maximum smoothness
                    .maxSizePercent(MEMORY_CACHE_PERCENT)
                    .strongReferencesEnabled(enable = true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            // Allow more concurrent loads for faster grid filling
            .crossfade(enable = true)
            .build()
    }
}
