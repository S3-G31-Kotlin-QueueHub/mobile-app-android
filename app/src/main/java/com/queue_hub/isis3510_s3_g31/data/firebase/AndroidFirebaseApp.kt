package com.queue_hub.isis3510_s3_g31.data.firebase

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class AndroidFirebaseApp: Application() {
    companion object {
        const val FCM_CHANNEL_ID = "FCM_CHANNEL_ID"
    }

    override fun onCreate() {
        super.onCreate()
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val fmcChannel = NotificationChannel(FCM_CHANNEL_ID, "FCM_CHANNEL_ID", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(fmcChannel)
        }
    }
}