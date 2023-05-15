package com.example.stayfinder.hotel.hotel_detail
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.stayfinder.R
import com.example.stayfinder.address
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SubHotelDetailAddress : Fragment(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap
    private lateinit var fusedClient: FusedLocationProviderClient;
    private lateinit var currentLocation : Location
    private var REQUEST_CODE = 101;

    lateinit var address : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_sub_hotel_detail_address, container, false)
//        val bookingDetail :HotelDetails? = this.getArguments()?.getSerializable("BookingDetail") as HotelDetails?
//        val addressTemp : address = bookingDetail!!.address

        address = this.arguments?.getString("hotel_address") as String
        val map_lat = this.requireArguments().getDouble("hotel_address_lat")
        val map_long = this.requireArguments().getDouble("hotel_address_long")

        val addressTv = view!!.findViewById<TextView>(R.id.addressTv)

        addressTv.text = address
        searchMap(map_lat, map_long)
        return view
    }

    private fun searchMap(lat: Double, long: Double){
//        val templatlng = getLocationFromAddress(address)
        currentLocation = Location(LocationManager.NETWORK_PROVIDER)
        currentLocation.latitude = lat
        currentLocation.longitude = long
        val addressestemp: List<Address>?
        val geocoder: Geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        val mapFragment:SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(REQUEST_CODE){
//            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
////                getLocationUser()
//                searchMap()
//            }
//        }
//    }

//    fun getLocationFromAddress( strAddress: String?): LatLng? {
//        val coder = Geocoder(this.requireContext(), Locale.getDefault())
//        val address: List<Address>?
//        var LatLan: LatLng? = null
//        try {
//            // May throw an IOException
//            address = coder.getFromLocationName(strAddress!!, 1)
//            if (address == null) {
//                return null
//            }
//            val location = address[0]
//            LatLan = LatLng(location.latitude, location.longitude)
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//        }
//        return LatLan
//    }
    override fun onMapReady(googleMap: GoogleMap) {//        mMap = googleMap
        val latLng = LatLng(currentLocation.latitude,currentLocation.longitude)
        val marketOptions = MarkerOptions().position(latLng).title(address)
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))
        googleMap.addMarker(marketOptions)
        googleMap.setOnMapClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("address",address)
            startActivity(intent)
        }
    }
}