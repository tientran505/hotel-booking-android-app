package com.example.stayfinder.search.map

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stayfinder.BookingInformation
import com.example.stayfinder.R
import com.example.stayfinder.databinding.ActivitySearchByMapBinding
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class SearchByMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySearchByMapBinding
    private var selectedMarker: Marker? = null

    private var hotels: ArrayList<HotelDetailModel> = ArrayList()

    private var startDate: Long = 0
    private var endDate: Long = 0
    private lateinit var bookingInformation: BookingInformation
    private lateinit var chosenCity: String
    private lateinit var header: String

    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchByMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()

        startDate = intent.getLongExtra("start_date", 0)
        endDate = intent.getLongExtra("end_date", 0)
        bookingInformation = intent.getSerializableExtra("booking_info") as BookingInformation
        chosenCity = intent.getStringExtra("city") as String
        header = intent.getStringExtra("header") as String

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapSearch) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fetchData(bookingInformation.sum_people, startDate, chosenCity)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.item_search_map, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun fetchData(guest: Int, start_date: Long, city: String) {
        db.collection("hotels")
            .whereEqualTo("address.city", city)
            .get()
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
                                    && guest <= room.guest_available) {
                                    hotels.add(hotel)
                                    break;
                                }
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                addMarkersToMap()
                            }
                        }
                }
            }
    }

    private suspend fun addMarkersToMap() {
        var latSum = 0.0
        var lngSum = 0.0

        for (hotel in hotels) {

            val minPrice = hotel.getMinPriceOfHotel(currentGuest = bookingInformation.sum_people)
            val location = LatLng(hotel.map[0], hotel.map[1])
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(minPrice)

            latSum += location.latitude
            lngSum += location.longitude

            withContext(Dispatchers.Main) {
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(formattedPrice)
                        .icon(createHotelPriceIcon(formattedPrice, 0))
                )
                marker?.tag = hotel
            }

        }

        val avgLat = latSum / hotels.size
        val avgLng = lngSum / hotels.size
        val avgLatLng = LatLng(avgLat, avgLng)

        withContext(Dispatchers.Main) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(avgLatLng, 12f))
            mMap.setOnMarkerClickListener(this@SearchByMapActivity)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12f))
//        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        selectedMarker?.setIcon(createHotelPriceIcon(selectedMarker!!.title!!, 2))
        marker.setIcon(createHotelPriceIcon(marker.title!!, 1))
        selectedMarker = marker

//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
        val hotel = marker.tag as HotelDetailModel
        replaceFragment(HotelMapSearch(hotel, db, bookingInformation.sum_people, startDate, endDate, bookingInformation))

        return true
    }

    private fun createHotelPriceIcon(price: String, isSelected: Int): BitmapDescriptor {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_info_window, null)
        val textView = view.findViewById<TextView>(R.id.tv_hotel_price)
        textView.text = price

        if (isSelected == 1) {
            val cardView = view.findViewById<CardView>(R.id.cardView)
            cardView.setCardBackgroundColor(Color.parseColor("#FF2C82FB"))
            textView.textSize = 13F
        } else if (isSelected == 2) {
            val cardView = view.findViewById<CardView>(R.id.cardView)
            cardView.setCardBackgroundColor(Color.parseColor("#FF424242"))
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Search by Map"
    }
}