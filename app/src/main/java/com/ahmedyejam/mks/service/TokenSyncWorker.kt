package com.ahmedyejam.mks.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ahmedyejam.mks.util.MksLogger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TokenSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Cloud token sync backend is not yet available.
        // Returning failure (not success) so WorkManager does not record this as completed.
        // When a backend is introduced, implement token retrieval + sync here.
        MksLogger.w("TokenSyncWorker", "Token sync skipped — no backend configured.")
        return Result.failure()
    }
}
