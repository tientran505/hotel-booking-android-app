package com.example.stayfinder.services.hotel

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.R
import com.example.stayfinder.hotel.hotel_detail.SubHotelDetailAddress
import com.example.stayfinder.model.HotelDetailModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.w3c.dom.Text
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class AddHotelConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel_confirm)

        val fm: FragmentManager = supportFragmentManager

        var hotel = intent.getSerializableExtra("hotelInfo") as HotelDetailModel?
        var tempUriImage = intent.getStringArrayListExtra("uriImage")
        var uuidHotel = hotel!!.id



        var addressTemp = hotel.address
        var address =  addressTemp["number"].toString()+" "+ addressTemp["street"]+", "+ addressTemp["district"]+", "+ addressTemp["ward"]+", "+addressTemp["city"]
        findViewById<TextView>(R.id.addressTv).text = address

        //save latitude longitude to hotel.map
        var latitude = intent.getDoubleExtra("latitude", 0.0)
        var longitude = intent.getDoubleExtra("longitude", 0.0)

        if(latitude == 0.0 && longitude == 0.0){ // lấy địa chỉ tạm
            var latlan = getLocationFromAddress(address)!!
            hotel.map = arrayListOf(latlan.latitude, latlan.longitude)
            latitude = latlan.latitude
            longitude = latlan.longitude
        }
        else{
            hotel.map = arrayListOf(latitude, longitude)
        }


        val bundle = Bundle()
        bundle.putSerializable("BookingDetail", hotel)
        bundle.putDouble("latitude", latitude)
        bundle.putDouble("longitude", longitude)

        val fragAddress = SubAddressFragment()
        fragAddress.arguments = bundle;


        fm.beginTransaction().replace(R.id.frameLayout2, fragAddress).commit();


    }

    fun getLocationFromAddress( strAddress: String?): LatLng? {
        val coder = Geocoder(this, Locale.getDefault())
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
}