package com.example.stayfinder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.booking.PersonalConfirmation
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.hotel.HotelAdapter
import com.example.stayfinder.search.HotelSearchAdapter

class HotelSearch : AppCompatActivity() {
    lateinit var searchBar: TextView
    lateinit var hotelSearchRV: RecyclerView
    lateinit var hotelSearchAdapter: HotelSearchAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_search)

        supportActionBar?.hide()

        searchBar = findViewById(R.id.searchBar)
        Toast.makeText(this, searchBar.text.toString(), Toast.LENGTH_SHORT).show()

        searchBar.setOnClickListener {
            Toast.makeText(this, "Search bar clicked", Toast.LENGTH_SHORT).show()
        }

        searchBar.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (motionEvent.rawX <= searchBar.totalPaddingLeft) {
                    finish()
                    true
                }
            }
            false
        }



        initRV()
    }

    fun initRV() {
        val hotels = listOf(
            Hotel("Vung Tau", "Sherwood Residence", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("Phan Thiet", "Somerset Ho Chi Minh City", 5.0.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("Vung Tau", "SILA Urban Living", 3.3.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("Da Nang", "La vela Saigon Hotel", 2.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("Vung Tau", "Novotel Saigon", 3.0.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("Ha Noi", "Villa Song Saigon", 4.8.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("Hai Phong", "Norfolk mansion - Luxury Service Ap..", 50.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("Phan Thiet", "CityHouse - Ariosa", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("Da Lat", "Sherwood Residence", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("Vung Tau", "Sherwood Residence", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
        )


        hotelSearchRV = findViewById(R.id.hotelSearchRV)
        hotelSearchRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL
            , false)

        hotelSearchAdapter = HotelSearchAdapter(hotels, this)
        hotelSearchAdapter.onItemClick = {position ->

        }

        hotelSearchAdapter.onButtonClick = {position ->
            hotelSearchAdapter.setHeart(position)
        }

        hotelSearchAdapter.onItemClick = {position ->
            val intent = Intent(this, PersonalConfirmation::class.java)
            startActivity(intent)
        }

        hotelSearchRV.adapter = hotelSearchAdapter

    }
}