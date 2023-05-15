package com.example.stayfinder

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.stayfinder.partner.booking.PartnerBookingDetail
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.stayfinder"
class MyFirebaseMessagingService : FirebaseMessagingService(){

    fun getRemoteView(title:String, message: String):RemoteViews{
        val remoteViews = RemoteViews(channelName,R.layout.notification)
        remoteViews.setTextViewText(R.id.titleTv, title)
        remoteViews.setTextViewText(R.id.messTv,message)
        return remoteViews
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null ){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }

    }

    @SuppressLint("RemoteViewLayout")
    fun generateNotification(title:String,message:String){
        val intent = Intent(this,PartnerBookingDetail::class.java)
        val booking_id=""
        intent.putExtra("booking_id",booking_id)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_message)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())

    }
}