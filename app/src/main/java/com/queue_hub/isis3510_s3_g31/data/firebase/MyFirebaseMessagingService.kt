package com.queue_hub.isis3510_s3_g31.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.queue_hub.isis3510_s3_g31.R
import com.queue_hub.isis3510_s3_g31.ui.navigation.Home
import kotlin.random.Random

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val random = Random

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let { message ->
            val title = message.title
            val body = message.body

            sendNotification(title, body)
            // Handle the received message here
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, Home::class.java ).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.notification_fcm)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.queuehub)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        manager.notify(random.nextInt(), notificationBuilder.build())

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }

}