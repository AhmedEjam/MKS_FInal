package com.ahmedyejam.mks

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MksApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        com.tom_roush.pdfbox.android.PDFBoxResourceLoader.init(this)
        com.ahmedyejam.mks.data.importer.xlsx.PoiInitializer.init()
        setupCrashHandler()
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            com.ahmedyejam.mks.util.MksLogger.e(
                "MksApplication",
                "Uncaught exception in thread ${thread.name}",
                throwable
            )
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    companion object {
        private const val MEMORY_CACHE_PERCENT = 0.25
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
