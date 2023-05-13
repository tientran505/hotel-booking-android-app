package com.example.stayfinder.services.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.NotificationModel
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

    companion object{
        val SENDER_ID = "StayFinderHotel"
    }

    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore
    var collection_name_token_notification:String? = null

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        collection_name_token_notification = getString(R.string.collection_name_token_notification)
//        var notification: RemoteMessage.Notification? = message.notification ?: return
//
//        var strTitle = notification!!.title!!
//        var strBody = notification.body!!
        var stringMap: Map<String, String>? = message.data

        if (stringMap == null) return

        val strTitle = stringMap.get("uuidUser")
        val strBody= stringMap.get("body")

        if (strTitle != null && strBody != null) {
            sendNotification(strTitle, strBody)
        }
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

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    fun getToken(): String{
        return FirebaseMessaging.getInstance().token.toString()
    }

    fun getToken(uuid:String):String?{
        var result : String? = null
        collection_name_token_notification = getString(R.string.collection_name_token_notification)
        db.collection(collection_name_token_notification!!).document(uuid).get().addOnSuccessListener {
            document ->
            if(document!= null) {
                val message = document.toObject(NotificationModel::class.java)
                result = message!!.tokenUser
            }

        }
        return result
    }

    fun sendMessageToServer(title:String, message:String, token:String){
        val fm = Firebase.messaging
        fm.send(
            remoteMessage("$token@fcm.googleapis.com") {
                setMessageId(messageId.toString())
                addData("strTitle", title)
                addData("body", message)
            },
        )
    }
}