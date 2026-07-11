package com.ahmedyejam.mks.service

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ahmedyejam.mks.data.preferences.DataStoreManager
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

    override fun onMessageReceived(message: RemoteMessage) {
        // Enforce data-only payload processing
        if (message.data.isNotEmpty()) {
            val messageId = message.messageId ?: return
            
            serviceScope.launch {
                // TODO: Insert into Room for Idempotency check. 
                // e.g., val inserted = database.notificationDao().insertPayload(messageId)
                // if (inserted) { processPayload() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
