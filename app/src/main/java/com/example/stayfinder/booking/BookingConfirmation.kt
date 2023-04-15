package com.example.stayfinder.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.stayfinder.R
import com.example.stayfinder.booking.adapter.HotelPriceList
import com.example.stayfinder.booking.adapter.PriceDetailConfirmListAdapter
import com.example.stayfinder.booking.adapter.Room
import com.example.stayfinder.booking.adapter.RoomConfirmListAdapter

class BookingConfirmation : AppCompatActivity() {
    // hotel view
    private lateinit var hotelImg: ImageView
    private lateinit var hotelType: TextView
    private lateinit var hotelName: TextView
    private lateinit var address: TextView

    // staying time view
    private lateinit var checkinDate: TextView
    private lateinit var checkinTime: TextView
    private lateinit var checkoutDate: TextView
    private lateinit var checkoutTime: TextView
    private lateinit var nightDays: TextView

    // Guest detail view
    private lateinit var guestName: TextView
    private lateinit var guestPhone: TextView
    private lateinit var guestEmail: TextView
    private lateinit var numberOfPeople: TextView

    // Hotel list view confirm
    private lateinit var roomLV: ListView

    // special request
    private lateinit var notes: EditText

    // price detail view
    private lateinit var priceHotelDetailLV: ListView
    private lateinit var originalPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var totalPrice: TextView

    // bottom component
    private lateinit var originalSum: TextView
    private lateinit var discountSum: TextView
    private lateinit var bookBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation)

        initRoomConfirmListView()
        initHotelView()
        initStayingTimeView()
        initGuestDetailView()
        initNoteView()
        initPriceDetailView()
        initBottomComponent()
        initActionBar()

        bookBtn.setOnClickListener {
            Toast.makeText(this, "Room booked successfully", Toast.LENGTH_LONG).show()
        }
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Final step: Booking confirmation"
    }


    private fun initHotelView() {
        hotelImg = findViewById(R.id.hotelImgConfirm)
        hotelType = findViewById(R.id.hotelType)
        hotelName = findViewById(R.id.hotelNameBC)
        address = findViewById(R.id.addressBC)
    }

    private fun initStayingTimeView() {
        checkinDate = findViewById(R.id.checkinDateTV)
        checkinTime = findViewById(R.id.checkinTimeTV)
        checkoutDate = findViewById(R.id.checkoutDateTV)
        checkoutTime = findViewById(R.id.checkoutTimeTV)
        nightDays = findViewById(R.id.nightDays)
    }

    private fun initGuestDetailView() {
        guestName = findViewById(R.id.guestNameTV)
        guestPhone = findViewById(R.id.guestPhoneTV)
        guestEmail = findViewById(R.id.guestMailTV)
        numberOfPeople = findViewById(R.id.pplInformation)
    }

    private fun initRoomConfirmListView() {
        val mList = arrayListOf<Room>(
            Room("One-Bedroom Apartment", "", 4),
            Room("Apartment with Balcony", "", 5),
            Room("Apartment with Balcony", "", 5)
        )

        roomLV = findViewById(R.id.roomLV)

        val roomConfirmListAdapter = RoomConfirmListAdapter(this, mList)
        roomLV.adapter = roomConfirmListAdapter
        roomConfirmListAdapter.notifyDataSetChanged()
        roomLV.visibility = View.GONE
        roomLV.visibility = View.VISIBLE
    }

    private fun initPriceConfirmListView() {
        val mList = arrayListOf<HotelPriceList>(
            HotelPriceList("One-Bedroom Apartment", 4500000.0.toFloat(),
                3200000.0.toFloat()),
            HotelPriceList("Apartment with Balcony", 4500000.0.toFloat(),
                3200000.0.toFloat()),
            HotelPriceList("Apartment with Balcony", 4500000.0.toFloat(),
                3200000.0.toFloat())
        )

        priceHotelDetailLV = findViewById(R.id.priceDetailLV)
        val priceDetailAdapter = PriceDetailConfirmListAdapter(this, mList)

        priceHotelDetailLV.adapter = priceDetailAdapter
    }

    private fun initNoteView() {
        notes = findViewById(R.id.noteET)
    }

    private fun initPriceDetailView() {
        initPriceConfirmListView()
        originalPrice = findViewById(R.id.originalConfirmPriceTV)
        discountPrice = findViewById(R.id.discountConfirmPriceTV)
        totalPrice = findViewById(R.id.totalPriceConfirm)
    }

    private fun initBottomComponent() {
        originalSum = findViewById(R.id.oPriceTV)
        discountSum = findViewById(R.id.dcPriceTV)
        bookBtn = findViewById(R.id.nextBtn)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}