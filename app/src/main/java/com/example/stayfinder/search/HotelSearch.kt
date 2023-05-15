package com.example.stayfinder.search

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.BookingInformation
import com.example.stayfinder.R
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.hotel.hotel_detail.HotelDetailActivity
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.saved.choose_item.SavedListChooseBottomSheetDialog
import com.example.stayfinder.search.map.SearchByMapActivity
import com.example.stayfinder.search.sort.SortListFragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class HotelSearch : AppCompatActivity() {
    lateinit var searchBar: TextView
    lateinit var hotelSearchRV: RecyclerView
    lateinit var hotelSearchAdapter: HotelSearchAdapter

    lateinit var sortBtn: Button
    lateinit var filterBtn: Button
    lateinit var mapBtn: Button

    private var hotelList: ArrayList<HotelDetailModel> = ArrayList()

    private var startDate: Long = 0
    private var endDate: Long = 0
    private lateinit var bookingInformation: BookingInformation

    val db = Firebase.firestore


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_search)

        supportActionBar?.hide()

        startDate = intent.getLongExtra("start_date", 0)
        endDate = intent.getLongExtra("end_date", 0)
        bookingInformation = intent.getSerializableExtra("booking_info") as BookingInformation

        searchBar = findViewById(R.id.searchBar)
        Toast.makeText(this, searchBar.text.toString(), Toast.LENGTH_SHORT).show()

//        searchBar.setOnClickListener {
//            Toast.makeText(this, "Search bar clicked", Toast.LENGTH_SHORT).show()
//        }

        searchBar.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (motionEvent.rawX <= searchBar.totalPaddingLeft) {
                    finish()
                    true
                }
            }
            false
        }

        sortBtnHandle()
        mapBtnHandler()
        initRV()
        fetchData(bookingInformation.sum_people, startDate)
    }

    private fun sortBtnHandle() {
        sortBtn = findViewById(R.id.sortBtn)
        sortBtn.setOnClickListener {
            val searchBottomSheet = SortListFragment()
            searchBottomSheet.show(this.supportFragmentManager
                , SortListFragment.TAG)
        }
    }

    private fun mapBtnHandler() {
        mapBtn = findViewById(R.id.mapBtn)
        mapBtn.setOnClickListener {
            startActivity(Intent(this, SearchByMapActivity::class.java))
        }


    }

    private fun fetchData(guest: Int, start_date: Long) {
        db.collection("hotels").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val hotel = document.toObject(HotelDetailModel::class.java)

                    db.collection("rooms")
                        .whereEqualTo("hotel_id", hotel.id)
                        .get()
                        .addOnSuccessListener { docs ->
                            for (doc in docs) {
                                val room = doc.toObject(RoomDetailModel::class.java)

                                if (room.room_available > 0 && guest >= room.min_guest
                                    && guest <= room.guest_available
                                    && room.available_start_date!!.toDate().time <= start_date) {
                                    hotelList.add(hotel)
                                    hotelSearchAdapter.notifyItemInserted(hotelList.size - 1)
                                    break;
                                }
                            }
                        }
                }
            }
    }

    private fun initRV() {

        hotelSearchRV = findViewById(R.id.hotelSearchRV)
        hotelSearchRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL
            , false)

        hotelSearchAdapter = HotelSearchAdapter(hotelList, this, bookingInformation.sum_people, db)
        hotelSearchAdapter.onItemClick = {position ->
            val intent = Intent(this, HotelDetailActivity::class.java)

            intent.putExtra("start_date", startDate)
            intent.putExtra("end_date", endDate)
            intent.putExtra("booking_info", bookingInformation)
            intent.putExtra("hotel_id", hotelList[position].id)

            startActivity(intent)
        }

        hotelSearchAdapter.onButtonClick = {position ->
//            hotels[position].isSaved = !hotels[position].isSaved

            val collectionBottomSheet = SavedListChooseBottomSheetDialog("eb875113-c692-4219-b78e-59a016c625be"
                ,"Melody Vũng Tàu"
                , "https://firebasestorage.googleapis.com/v0/b/hotel-booking-app-b6d5b.appspot.com/o/imgsTest%2Feb875113-c692-4219-b78e-59a016c625be-0?alt=media&token=d9ff197d-d9e1-4925-971a-3031c7b21fb1")
            collectionBottomSheet.show(this.supportFragmentManager
                , SavedListChooseBottomSheetDialog.TAG)

        }

        hotelSearchRV.adapter = hotelSearchAdapter

    }
}