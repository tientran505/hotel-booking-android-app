package com.example.stayfinder

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.stayfinder.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedClient: FusedLocationProviderClient;
    private lateinit var currentLocation : Location
    private var REQUEST_CODE = 101;

    val address = "135 Đường Trần Hưng Đạo, Cầu Ông Lãnh, Quận 1, Thành phố Hồ Chí Minh, Việt Nam"
    var addressfind =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        searchMap()
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
                Toast.makeText(applicationContext,currentLocation.latitude.toString()+" "+currentLocation.longitude.toString(),Toast.LENGTH_LONG).show()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
                print(location.toString())
            }
        }

    }
    fun searchMap(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)
            return
        }

        val templatlng = getLocationFromAddress(address)

        if(templatlng != null){
            currentLocation = Location(LocationManager.NETWORK_PROVIDER)
            currentLocation.latitude = templatlng.latitude
            currentLocation.longitude = templatlng.longitude


            val geocoder: Geocoder
            val addressestemp: List<Address>?
            geocoder = Geocoder(this, Locale.getDefault())

            addressestemp = geocoder.getFromLocation(
                templatlng.latitude,
                templatlng.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressfind=""
            addressfind += addressestemp!![0].getAddressLine(0)// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            addressfind+= addressestemp!![0].locality
            addressfind+= addressestemp!![0].adminArea
            addressfind+= addressestemp!![0].countryName
            addressfind+= addressestemp!![0].postalCode
            addressfind+= addressestemp!![0].featureName

            Toast.makeText(applicationContext,addressfind,Toast.LENGTH_LONG).show()
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
//                getLocationUser()
                searchMap()
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
            Toast.makeText(applicationContext,addressList[0].toString(),Toast.LENGTH_LONG).show()
            val location = addressList[0]
            LatLan = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return LatLan
    }
    override fun onMapReady(googleMap: GoogleMap) {

        val latLng = LatLng(currentLocation.latitude,currentLocation.longitude)
        val marketOptions = MarkerOptions().position(latLng).title(addressfind)

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))
        googleMap?.addMarker(marketOptions)
    }
}