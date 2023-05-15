package com.example.stayfinder.services.notification

import android.app.Activity
import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.stayfinder.R
import org.json.JSONException
import org.json.JSONObject


class FcmNotificationSender (var userFcmToken:String, var   title: String,var   body:String, var booking_id: String ,var mContext: Context,var mActivity:Activity) {
//    lateinit var userFcmToken:String
//    lateinit var title: String
//    lateinit var body :String
//    lateinit var mContext:Context
//    lateinit var mActivity:Activity


    private lateinit var requestQueue: RequestQueue
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey:String = "AAAAv7e9VnI:APA91bGWJ_MICzBN0KIb6aZ4bromjnmNm7iNlJRn4FErP_KY6M4AOOn9s5g1AywIuCg7BLc-rmzJ3SCI9A4cFbzUzMCEHr_pMELaCBW5pLWW60zKkue4wm08XxT8ELSUv0fFj_EDH_6C"


//    fun FcmNotificationsSender(userFcmToken:String,  title: String,  body:String,  mContext: Context,  mActivity:Activity){
//        this.userFcmToken = userFcmToken;
//        this.title = title;
//        this.body = body;
//        this.mContext = mContext;
//        this.mActivity = mActivity;
//    }

    fun SendNotifications(){
        requestQueue = Volley.newRequestQueue(mActivity)
        val mainObj = JSONObject()


        try {
            mainObj.put("to", userFcmToken)

            var notiObj = JSONObject()
            notiObj.put("title", title)
            notiObj.put("body", body)
            notiObj.put("icon", R.drawable.ic_message)
            notiObj.put("tag", booking_id)
            mainObj.put("notification", notiObj)

            var request = object :  JsonObjectRequest(Request.Method.POST, postUrl, mainObj,
                {
                    // code run is got response
                },
                {
                    // code run is got error
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String>? {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=$fcmServerKey"
                    return header
                }
            }

            requestQueue.add(request);

        }
        catch (e: JSONException){
            e.printStackTrace();
        }
    }





}