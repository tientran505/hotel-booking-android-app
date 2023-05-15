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
import com.example.stayfinder.R
import com.example.stayfinder.databinding.ActivitySearchByMapBinding
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.model.HotelDetailModel

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
import java.lang.Math.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class SearchByMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySearchByMapBinding
    private var selectedMarker: Marker? = null

    val db = Firebase.firestore

    private var hotels = ArrayList<HotelDetailModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchByMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapSearch) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fetchData()
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

    private fun fetchData() {
        db.collection("hotels")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val hotel = document.toObject(HotelDetailModel::class.java)
                    hotels.add(hotel)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    addMarkersToMap()
                }
            }
    }

    private suspend fun addMarkersToMap() {
        for (hotel in hotels) {

            val minPrice = hotel.getMinPriceOfHotel(7)
            val location = LatLng(hotel.map[0], hotel.map[1])
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(minPrice)

            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(formattedPrice)
                    .icon(createHotelPriceIcon(formattedPrice, 0))
            )
        }
        mMap.setOnMarkerClickListener(this)
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
        val i = Random.nextInt(0, hotels.size - 1)
        replaceFragment(HotelMapSearch(hotels[i], db, 8))

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

    private fun randomInRange(min: Int, max: Int): Int {
        return Random.nextInt(min, max + 1)
    }

    private fun randomLocationNearby(center: LatLng, radius: Double): LatLng {
        val randomRadius = radius * kotlin.math.sqrt(Random.nextDouble())
        val randomAngle = Random.nextDouble() * 2 * PI

        val offsetX = randomRadius * kotlin.math.cos(randomAngle)
        val offsetY = randomRadius * kotlin.math.sin(randomAngle)

        val offsetXInDegrees = offsetX / 111320.0
        val offsetYInDegrees = offsetY / (40075000.0 * kotlin.math.cos(center.latitude * PI / 180) / 360)

        return LatLng(center.latitude + offsetYInDegrees, center.longitude + offsetXInDegrees)
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