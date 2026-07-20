package com.ahmedyejam.mks.service

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.util.MksLogger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "AppFirebaseMessagingService"
    }

    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var workManager: WorkManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        serviceScope.launch {
            // 1. Cache Token Locally Instantly
            dataStoreManager.setFcmToken(token)
            
            // 2. Enqueue Background Work to sync with backend when network is available
            val syncWork = OneTimeWorkRequestBuilder<TokenSyncWorker>()
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .build()
                
            workManager.enqueueUniqueWork(
                "FcmTokenSync",
                ExistingWorkPolicy.REPLACE,
                syncWork
            )
        }
    }

    /**
     * Data payloads are received but **not yet acted on**.
     *
     * Previously this launched an empty coroutine, which read as working code while doing nothing.
     * Delivering payloads needs a de-duplication store first: FCM can deliver the same `messageId`
     * more than once, so processing without an idempotency check would double-apply whatever the
     * payload asks for. Tracked in `docs/roadmap.md` §1.7.
     *
     * The receipt is logged so a payload arriving during testing is visible rather than vanishing.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        // Enforce data-only payload processing
        if (message.data.isEmpty()) return
        val messageId = message.messageId ?: return

        MksLogger.d(TAG, "FCM data payload $messageId received; no handler wired yet")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
