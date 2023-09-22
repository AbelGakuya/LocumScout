package com.locums.locumscout.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.locums.locumscout.MainActivity
import com.locums.locumscout.R
import com.locums.locumscout.data.IncomingNotificationData
import com.locums.locumscout.other.Constants.ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT

private const val CHANNEL_ID = "my_channel"
class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    private var notificationIdCounter = 0

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        val notificationData = message.data
        val notificationTitle = notificationData["title"]
        val notificationMessage = notificationData["message"]
        val applicantId = notificationData["applicantId"]
        val hospitalId = notificationData["hospitalId"]
        val timestamp = com.google.firebase.Timestamp.now()
        val read: Boolean = false

        val firestore = FirebaseFirestore.getInstance()
        val notificationsCollection = firestore.collection("doctor_users")
            .document(applicantId!!).collection("notifications")

        val notificationId = notificationsCollection.document().id
        //create the incoming NotificationData
        val incomingNotificationData = IncomingNotificationData(notificationTitle,notificationMessage,timestamp,applicantId, notificationId, read)

        //insert notification to firestore

        notificationsCollection.add(incomingNotificationData)
            .addOnSuccessListener {
                Log.d("FCM", "Notification saved to firestore")
            }
            .addOnFailureListener {
                Log.e("FCM","Error saving notification to firestore", it)
            }


//        CoroutineScope(Dispatchers.IO).launch {
//            repository.insertNotification(hospitalId,incomingNotificationData)
//        }

        //Handle the notification as needed
        //  handleNotification(incomingNotificationData)


        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("notificationType", "doctorDetails")
        intent.putExtra("notificationTitle", notificationTitle)
        intent.putExtra("notificationMessage", notificationMessage)

        intent.action = ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT


//        intent.putExtra("date", message.data["date"])
//        intent.putExtra("docName", message.data["docName"])
//        intent.putExtra("startTime", message.data["startTime"])
//        intent.putExtra("endTime", message.data["endTime"])

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = kotlin.random.Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_notifications)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)




   }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    }