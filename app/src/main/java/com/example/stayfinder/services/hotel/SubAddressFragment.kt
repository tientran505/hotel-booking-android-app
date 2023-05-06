package com.example.stayfinder.services.hotel

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.stayfinder.R
import com.example.stayfinder.address
import com.example.stayfinder.hotel.hotel_detail.HotelDetails
import com.example.stayfinder.hotel.hotel_detail.MapsActivity
import com.example.stayfinder.model.HotelDetailModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class SubAddressFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedClient: FusedLocationProviderClient;
    private lateinit var currentLocation : Location
    private var REQUEST_CODE = 101;

    var address = ""
    var addressfind =""
    var latitude = 0.0
    var longitude = 0.0

    var latitudeTemp = 0.0
    var longitudeTemp = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_sub_hotel_detail_address, container, false)
        val bookingDetail  = this.getArguments()?.getSerializable("BookingDetail") as HotelDetailModel?
        latitudeTemp = requireArguments().getDouble("latitude")
        longitudeTemp = requireArguments().getDouble("longitude")
        val addressTemp  = bookingDetail!!.address

        address = addressTemp["number"].toString()+" "+ addressTemp["street"]+", "+ addressTemp["district"]+", "+ addressTemp["ward"]+", "+addressTemp["city"]
        val addressTv = view!!.findViewById<TextView>(R.id.addressTv)
        addressTv.setText(address)
        searchMap()
        return view
    }


    fun searchMap(){
        if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE)
            return
        }
        //val templatlng = getLocationFromAddress(address)
        val templatlng = LatLng(latitudeTemp, longitudeTemp)
        if(templatlng != null){
            currentLocation = Location(LocationManager.NETWORK_PROVIDER)
            currentLocation.latitude = templatlng.latitude
            currentLocation.longitude = templatlng.longitude

            latitude = templatlng.latitude
            longitude = templatlng.longitude

            val geocoder: Geocoder
            val addressestemp: List<Address>?
            geocoder = Geocoder(this.requireContext(), Locale.getDefault())

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

            val mapFragment: SupportMapFragment =getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
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
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                getLocationUser()
                searchMap()
            }
        }
    }
    fun getLocationFromAddress( strAddress: String?): LatLng? {
        val coder = Geocoder(this.requireContext(), Locale.getDefault())
        val address: List<Address>?
        var LatLan: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress!!, 1)
            if (address == null) {
                return null
            }
            val location = address[0]
            LatLan = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return LatLan
    }
    override fun onMapReady(googleMap: GoogleMap) {//        mMap = googleMap
        val latLng = LatLng(currentLocation.latitude,currentLocation.longitude)
        val marketOptions = MarkerOptions().position(latLng).title(addressfind)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))
        googleMap?.addMarker(marketOptions)

        googleMap.setOnMapClickListener {
            val intent = Intent(this.context, MapAddHotelActivity::class.java)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude", longitude)
            startActivity(intent)
        }
    }
}