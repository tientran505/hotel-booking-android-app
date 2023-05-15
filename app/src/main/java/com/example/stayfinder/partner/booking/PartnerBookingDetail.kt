package com.example.stayfinder.partner.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.Booking
import com.example.stayfinder.BookingDetail
import com.example.stayfinder.R
import com.example.stayfinder.SavedListItem
import com.example.stayfinder.partner.booking.adapter.BookingRoomAdapter
import com.example.stayfinder.partner.booking.adapter.room_book
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PartnerBookingDetail : AppCompatActivity() {
    val db = Firebase.firestore
    var booking_id = ""
    lateinit var booking:BookingDetail
    private fun getBookingInfo(id:String, callback: () -> Unit){
        db.collection("bookings").document(id).get()
            .addOnSuccessListener {
                booking = it.toObject<BookingDetail>()!!
                callback.invoke()
            }
            .addOnFailureListener {  }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_booking_detail)

        //booking_id = intent.getStringExtra("booking_id")!!
        booking_id = "e226dER1qHNTalZsShH4"
        getBookingInfo(booking_id){
            var guestName = findViewById<TextView>(R.id.guestName)
            guestName.setText(booking.personal_contact!!.name)
            var guestPhoneNumber = findViewById<TextView>(R.id.guestPhoneNumber)
            guestPhoneNumber.setText(booking.personal_contact!!.phone_number)
            var guestEmail = findViewById<TextView>(R.id.guestEmail)
            guestEmail.setText(booking.personal_contact!!.email)

            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            var checkinDateTv = findViewById<TextView>(R.id.checkinDateTV)
            val startDate = sdf.format(Date(booking.date_start!!.seconds * 1000))
            checkinDateTv.setText(startDate)

            var checkoutDateTv = findViewById<TextView>(R.id.checkoutDateTV)
            val endDate = sdf.format(Date(booking.date_end!!.seconds * 1000))
            checkoutDateTv.setText(endDate)

            val startDateTime = Calendar.getInstance()
            startDateTime.time = sdf.parse(startDate)!!
            val endDateTime = Calendar.getInstance()
            endDateTime.time = sdf.parse(endDate)!!
            val daysBetween = TimeUnit.MILLISECONDS.toDays(endDateTime.timeInMillis - startDateTime.timeInMillis)
            var nightDays = findViewById<TextView>(R.id.nightDays)
            nightDays.setText(daysBetween.toString() + " nights")

            var roomList = arrayListOf<room_book>()
            for(r in booking.rooms){
                for (p in r.available_prices){
                    roomList.add(room_book(p.num_of_guest,p.price))
                }
            }

            var recyclerView = findViewById<RecyclerView>(R.id.recycler)
            var adapter = BookingRoomAdapter(roomList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)


        }
    }
}