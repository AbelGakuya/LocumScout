package com.locums.locumscout.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.locums.locumscout.R
import java.lang.Exception
import java.util.Random

class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    private var notificationIdCounter = 0

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()){
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]

            val notificationId = generateUniqueId()

            showNotification(notificationId,title,body)
        }

    }

    private fun generateUniqueId(): Int {
        return notificationIdCounter++

    }

    private fun showNotification(notificationId: Int, title: String?, body: String?) {
        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notifications)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    override fun onMessageSent(messageId: String) {
        super.onMessageSent(messageId)
    }

    override fun onSendError(messageId: String, exception: Exception) {
        super.onSendError(messageId, exception)
    }
}