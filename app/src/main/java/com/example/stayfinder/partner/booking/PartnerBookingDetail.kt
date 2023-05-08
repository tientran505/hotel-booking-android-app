package com.example.stayfinder.partner.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.Booking
import com.example.stayfinder.R

class PartnerBookingDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_booking_detail)

        var booking = intent.getSerializableExtra("booking") as com.example.stayfinder.partner.booking.adapter.Booking
    }
}