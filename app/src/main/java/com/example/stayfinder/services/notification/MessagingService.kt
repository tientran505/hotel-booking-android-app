package com.example.stayfinder.services.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        var notification: RemoteMessage.Notification? = message.notification ?: return

        var strTitle = notification!!.title!!
        var strBody = notification.body!!

        sendNotification(strTitle, strBody)
    }

    private fun sendNotification(strTitle: String, strBody: String) {
        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT)


        var notificationBuilder = NotificationCompat.Builder(this, "push_notification_id")
                                    .setContentTitle(strTitle)
                                    .setContentText(strBody)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentIntent(pendingIntent)

        var notification = notificationBuilder.build()
        var notificationManager : NotificationManager?  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager? ?: return

        if (notificationManager != null) {
            notificationManager.notify(1, notification)
        }

    }
}