package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.preview.CategoryMergePreviewService
import com.ahmedyejam.mks.data.preview.ClearMarksPreviewService
import com.ahmedyejam.mks.data.preview.DeletePreviewService
import com.ahmedyejam.mks.data.repair.AssetReferenceAuditService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltServiceModule {
    @Provides
    @Singleton
    fun provideDeletePreviewService(appModule: AppModule): DeletePreviewService {
        return appModule.deletePreviewService
    }

    @Provides
    @Singleton
    fun provideCategoryMergePreviewService(appModule: AppModule): CategoryMergePreviewService {
        return appModule.categoryMergePreviewService
    }

    @Provides
    @Singleton
    fun provideClearMarksPreviewService(appModule: AppModule): ClearMarksPreviewService {
        return appModule.clearMarksPreviewService
    }

    @Provides
    @Singleton
    fun provideAssetReferenceAuditService(appModule: AppModule): AssetReferenceAuditService {
        return appModule.assetReferenceAuditService
    }
}
