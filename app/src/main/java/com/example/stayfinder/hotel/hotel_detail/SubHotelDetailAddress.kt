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

/**
 * A simple [Fragment] subclass.
 * Use the [SubHotelDetailAddress.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubHotelDetailAddress : Fragment(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap
    private lateinit var fusedClient: FusedLocationProviderClient;
    private lateinit var currentLocation : Location
    private var REQUEST_CODE = 101;

    var address = ""
    var addressfind =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_sub_hotel_detail_address, container, false)
        val bookingDetail :HotelDetails? = this.getArguments()?.getSerializable("BookingDetail") as HotelDetails?
        val addressTemp : address = bookingDetail!!.address
        address = addressTemp.number.toString()+" "+ addressTemp.street+", "+ addressTemp.district+", "+ addressTemp.ward+", "+addressTemp.city
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
        val templatlng = getLocationFromAddress(address)
        if(templatlng != null){
            currentLocation = Location(LocationManager.NETWORK_PROVIDER)
            currentLocation.latitude = templatlng.latitude
            currentLocation.longitude = templatlng.longitude

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

            val mapFragment:SupportMapFragment =getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
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
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("address",address)
            startActivity(intent)
        }
    }


}