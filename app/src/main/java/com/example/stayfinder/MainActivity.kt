package com.example.stayfinder

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stayfinder.databinding.ActivityMainBinding
import com.example.stayfinder.model.NotificationModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.example.stayfinder.saved.SavedAnonymous
import com.example.stayfinder.services.login.ProfileFragment
import com.example.stayfinder.services.notification.FcmNotificationSender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Locale

//import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var myActionBar: ActionBar? = null
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        actionBarSetup()
        partnerAuth()
//        val uuidUser ="MlHDg6tlG2hXIWbmQTpQfxdW9Cx1"
//        db.collection(
//            getString(R.string.collection_name_token_notification)
//        ).document(uuidUser).get().addOnSuccessListener { document ->
//            if (document != null) {
//                var notificationObj = document.toObject(NotificationModel::class.java)
//                val token = notificationObj?.tokenUser
//                if (token != null) {
//                    val sender = FcmNotificationSender(
//                        token,
//                        "Booking ",
//                        " peoples",
//                        applicationContext,
//                        this
//                    )
//                    sender.SendNotifications()
//                }
//            }
//        }
//        val locale = Locale.ENGLISH
//        Locale.setDefault(locale)
//        val config = Configuration()
//        config.locale = locale
//        resources.updateConfiguration(config, resources.displayMetrics)

        val intent = intent
        val fragmentInfo = intent.getStringExtra("fragment")
        if (fragmentInfo != null) {
            if (fragmentInfo == "profile") {
                binding.bottomNavigationView.selectedItemId = R.id.profile
                replaceFragment(ProfileFragment())
            }
        }
        else {
            replaceFragment(HomeFragment())
        }


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                }

                R.id.saved -> {
                    if (user != null) {
                        replaceFragment(SavedFragment())
                    }
                    else {
                        replaceFragment(SavedAnonymous())
                    }
                }

                R.id.bookings -> {
                    replaceFragment(BookingFragment())
                }

                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_msg -> {
                Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show()
            }

            R.id.ic_save_hotel -> {
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSetup() {
        myActionBar = supportActionBar
        myActionBar?.title = "StayFinder"

        myActionBar?.setDisplayShowHomeEnabled(true)
        myActionBar?.setDisplayUseLogoEnabled(true)
    }

    private fun partnerAuth() {
        if (user != null) {
            val booking_id= intent.getStringExtra("booking_id")

            Toast.makeText(this, "user khac null", Toast.LENGTH_SHORT).show()
            Log.d("userisnull", "${user.uid} - ${user.displayName} - ${user.email}")
            val docRef = db.collection("users").document(user.uid).get()
            docRef.addOnCompleteListener { document ->
                val role = document.result.get("role")
                if (role != null && role == "partner") {
                    val intent = Intent(this, PartnerMainActivity::class.java)
                    if(booking_id!=null){
                        intent.putExtra("booking_id",booking_id)
                    }
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }
}