package com.example.stayfinder.partner.property.sub_property

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.stayfinder.R
import com.example.stayfinder.address
import com.example.stayfinder.databinding.ActivityEditLocationBinding
import com.example.stayfinder.hotels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*


class EditLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var progressBar: ProgressBar
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityEditLocationBinding
    private lateinit var fusedClient: FusedLocationProviderClient;
    private lateinit var currentLocation : Location
    private var REQUEST_CODE = 101;
    var address: String =""
    lateinit var addressFb :address
    lateinit var currentLocationBtn: Button
    lateinit var searchET: EditText
    lateinit var searchBtn: Button
    lateinit var saveBtn: Button
    var marker: Marker? = null
    private lateinit var defaultLocation: Location
    var lastKnownLocation: Location? = null
    private var locationPermissionGranted: Boolean= true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentLocationBtn = findViewById(R.id.currentLocationBtn)
        searchET = findViewById(R.id.searchET)
        searchBtn = findViewById(R.id.searchBtn)
        saveBtn = findViewById(R.id.saveBtn)
        progressBar = findViewById(R.id.savedListPB)
        progressBar.visibility = View.VISIBLE
        initActionBar()
        val bundle = intent.extras
        val hotel_id = bundle!!.getString("hotel_id")!!
        val db = Firebase.firestore;
        val documents = Firebase.firestore.collection("hotels")
            .document(hotel_id)
            .get()
            .addOnSuccessListener  { document ->
            if (document != null) {
                val hotels = document.toObject(hotels::class.java)!!
                val addressTemp = hotels?.address as address
                address = addressTemp.address
                searchMap(address)
                progressBar.visibility = View.GONE
            }
            else{
                address = "Việt Nam"
                searchMap(address)
                progressBar.visibility = View.GONE

            }
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        searchBtn.setOnClickListener {
            if(searchET.text.toString()!= ""&&searchET.text.toString()!= null){
                address = searchET.text.toString()
                searchMap(address)
            }
        }
        currentLocationBtn.setOnClickListener {
            getLocationUser()
        }
        saveBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            println(address)
            println(addressFb)
            val docRef = db.collection("Hotels").document(hotel_id)
            docRef.update("address", addressFb)
                .addOnSuccessListener {
                    // Update successful
                    progressBar.visibility = View.GONE

                }
                .addOnFailureListener { e ->
                    // Handle errors
                    progressBar.visibility = View.GONE

                }

        }
//        searchMap()
//        getLocationUser()
    }

    private fun convertLocationtoAddress(currentLocation: Location) {
        val geocoder: Geocoder =Geocoder(this, Locale.getDefault())
        val addressestemp: List<Address>?
        addressestemp = geocoder.getFromLocation(
            currentLocation.latitude,
            currentLocation.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        address = addressestemp!![0].getAddressLine(0)// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        addressFb = address(addressestemp[0].getAddressLine(0), addressestemp!![0].countryName )
    }

    private fun getLocationUser(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)
            return
        }
        val getLocation = fusedClient.lastLocation.addOnSuccessListener {
                location ->
            if(location!=null){
                currentLocation = location
                convertLocationtoAddress(currentLocation)
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

    }
    fun searchMap(address: String){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)
            return
        }

        val point = getLocationFromAddress(address)
        if(point != null){
            currentLocation = Location(LocationManager.NETWORK_PROVIDER)
            currentLocation.latitude = point.latitude
            currentLocation.longitude = point.longitude
            convertLocationtoAddress(currentLocation)
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(REQUEST_CODE){
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            }
        }
    }
    fun getLocationFromAddress( strAddress: String?): LatLng? {
        val coder = Geocoder(this, Locale.getDefault())
        val addressList: List<Address>?
        var LatLan: LatLng? = null
        try {
            addressList = coder.getFromLocationName(strAddress!!, 1)
            if (addressList == null) {
                return null
            }
            val location = addressList[0]
            println(location)
            addressFb = address(location.getAddressLine(0), location!!.countryName )
            LatLan = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return LatLan
    }
    override fun onMapReady(mMap: GoogleMap) {
        var latLng = LatLng(currentLocation.latitude,currentLocation.longitude)
        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))
        marker?.remove()
        marker?.isDraggable = true
        marker = mMap?.addMarker(MarkerOptions().position(latLng).title(address))
        mMap.setOnMapClickListener { point -> //save current location
            latLng = point
            var addresses: ArrayList<Address> = ArrayList()
            try {
                val geocoder: Geocoder =Geocoder(this, Locale.getDefault())
                addresses = geocoder.getFromLocation( point.latitude, point.longitude, 1) as ArrayList<Address>
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val addressTemp = addresses[0]
            println("Address[0]"+ addressFb)
            if (addressTemp != null) {
                address = addressTemp.getAddressLine(0)
                addressFb = address(addressTemp.getAddressLine(0),addressTemp.countryName)
                currentLocation = Location(LocationManager.NETWORK_PROVIDER)
                currentLocation.latitude = point.latitude
                currentLocation.longitude = point.longitude
            }
            //remove previously placed Marker
            //place marker where user just clicked
            marker?.remove()
            marker = mMap.addMarker(
                MarkerOptions().position(point).title(address)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
        }
    }
    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Edit Location"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}