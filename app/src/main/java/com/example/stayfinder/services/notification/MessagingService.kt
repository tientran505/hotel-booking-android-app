package com.example.stayfinder.services.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.NotificationModel
import com.example.stayfinder.partner.booking.PartnerBookingDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage

class MessagingService : FirebaseMessagingService() {

    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore
    var collection_name_token_notification:String? = null

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        collection_name_token_notification = getString(R.string.collection_name_token_notification)
        var notificationx: RemoteMessage.Notification? = message.notification ?: return
//
        var strTitle = notificationx!!.title!!
        var strBody = notificationx.body!!
        var booking_id = notificationx.tag!!

//        var stringMap: Map<String, String>? = message.data
//
//        if (stringMap == null) return
//
//        val strTitle = stringMap.get("title")
//        val strBody= stringMap.get("body")
//
//        if (strTitle != null && strBody != null) {
//            sendNotification(strTitle, strBody)
//        }

        //âm thanh thông báo
//        var notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        var r = RingtoneManager.getRingtone(applicationContext, notification)
//        r.play()

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//            r.isLooping = false
//        }

        //cài rung khi có thông báo
//        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        val pattern = longArrayOf(100,300,300,100)
//        v.vibrate(pattern,-1)
//
//        val resourceImage = resources.getIdentifier(message.notification?.icon,"drawable", packageName)
//
//        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
////        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
////
////        }
//        builder.setSmallIcon(resourceImage)

        sendNotification(strTitle, strBody,booking_id)

    }


    private fun sendNotification(strTitle: String, strBody: String,booking_id: String) {
        var intent = Intent(this, PartnerBookingDetail::class.java)
        intent.putExtra("booking_id",booking_id)
        var pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_IMMUTABLE)


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

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("DEBUG LOG================", token)
    }

}