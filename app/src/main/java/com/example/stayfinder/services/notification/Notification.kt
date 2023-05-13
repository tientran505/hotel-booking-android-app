package com.example.stayfinder.services.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class Notification : Application() {
    public var CHANNEL_ID = "push_notification_id"

    override fun onCreate() {
        super.onCreate()

        createChannelNotification()
    }

    private fun createChannelNotification() {

        var channel = NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_DEFAULT)
        var manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}