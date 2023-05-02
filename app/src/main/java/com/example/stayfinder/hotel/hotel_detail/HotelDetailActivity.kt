package com.example.stayfinder.hotel.hotel_detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.io.Serializable
import java.net.URL
data class hotelss(
    var id: String = "",
    var hotel_name: String = "",
    var description: String = "",
    var address: address = address(),
    var photoUrl: ArrayList<String> = ArrayList<String>(),
    var rating: rating = rating(),
    var rating_overall: Double = 0.0,
    val booking_count: Int = 0,
    val comment_count: Int = 0,
) :Serializable{
//    constructor(hotel_id:String, hotel_name:String, description:String, address:address, photoUrl: String,
//                facilities: ArrayList<facilities>) : this(hotel_id, hotel_name, description, address,
//        photoUrl, facilities, rating(0.0,0.0,0.0,0.0,), 0.0,
//        0, 0)
}
data class HotelDetails(
    var id: String = "",
    var hotel_name: String ="",
    var priceless: Double =0.0,
    var img: ArrayList<URL> =ArrayList<URL>(),
    var rating_overall: Double =0.0,
    var address: address = address(),
    var description: String="",
    var noFeedback: Int =0,
    val booking_count: Int =0,
    ):Serializable{
    constructor(id: String,hotel_name: String,pricebernight: Double, address: address,img: ArrayList<URL>,rating_overall: Double,description: String):
            this(id,hotel_name,pricebernight,img,rating_overall,address,description,0,0)
    constructor(a: hotelss):this(a.id,a.hotel_name,0.0, convertStringtoURL(a.photoUrl),a.rating_overall,a.address,a.description,a.comment_count,a.booking_count)
    constructor(a: hotelss, priceless: Double):this(a.id,a.hotel_name,priceless, convertStringtoURL(a.photoUrl),a.rating_overall,a.address,a.description,a.comment_count,a.booking_count)

}
fun convertStringtoURL(a: ArrayList<String>):ArrayList<URL>{
    var url = ArrayList<URL>()
    for(i in a){
        url.add(URL(i))
    }
    return url
}
class HotelDetailActivity : AppCompatActivity() , CoroutineScope by MainScope() {
    var hoteldetails:HotelDetails? = null
    var isPress = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initActionBar()
        val documents = Firebase.firestore.collection("Hotels")
            .document("5l5PibkyeRaZRFCVPrlB")
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                val l = document.toObject(hotelss::class.java)
                println(l)
                hoteldetails = l?.let { HotelDetails(it) }
                setContentView(R.layout.activity_hotel_detail)
                println(hoteldetails)
                val fm: FragmentManager = supportFragmentManager
                val dateStart = "30-3-2023"
                val dateEnd = "1-4-2023"
                val detailRoom = arrayListOf<Int>(1, 2, 0)
                val bundle = Bundle()
                bundle.putSerializable("BookingDetail", hoteldetails)
                bundle.putString("dateStart", dateStart)
                bundle.putString("dateEnd", dateEnd)
                bundle.putIntegerArrayList("detailRoom", detailRoom)
                val fragImage = SubHotelDetailImage()
                fragImage.setArguments(bundle);
                val fragPeriod = SubHotelDetailPeriod()
                fragPeriod.setArguments(bundle);
                val fragAddress = SubHotelDetailAddress()
                fragAddress.setArguments(bundle);
                val fragDescription = SubHotelDetailDescription()
                fragDescription.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fame1, fragImage).commit();
                fm.beginTransaction().replace(R.id.fame2, fragPeriod).commit();
                fm.beginTransaction().replace(R.id.fame3, fragAddress).commit();
                fm.beginTransaction().replace(R.id.fame4, fragDescription).commit();
                val bookingBtn = findViewById<Button>(R.id.BookingBtn)
                bookingBtn.setOnClickListener() {
                    val intent = Intent(this, RoomActivity::class.java)
                    intent.putExtra("hotel_id", hoteldetails?.id);
                    startActivity(intent)
                }
            } else {

            }
        }
            .addOnFailureListener { exception ->
            }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Hotel detail"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu_save, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.ic_save_hotel -> {
                Toast.makeText(this, "Nhu Y code o day",
                Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}