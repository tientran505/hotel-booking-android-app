package com.example.stayfinder

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.net.URL
data class hotelss(
    var hotel_id: String = "",
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
    var id: String ="",
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
    constructor(a: hotelss):this(a.hotel_id,a.hotel_name,0.0, convertStringtoURL(a.photoUrl),a.rating_overall,a.address,a.description,a.comment_count,a.booking_count)
    constructor(a: hotelss, priceless: Double):this(a.hotel_id,a.hotel_name,priceless, convertStringtoURL(a.photoUrl),a.rating_overall,a.address,a.description,a.comment_count,a.booking_count)

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val documents = Firebase.firestore.collection("Hotels")
            .document("5l5PibkyeRaZRFCVPrlB")
        documents.get().addOnSuccessListener { document ->
                if (document != null) {
                    val l = document.toObject(hotelss::class.java)
                    hoteldetails = l?.let { HotelDetails(it) }
                    setContentView(R.layout.activity_hotel_detail)
                    println(hoteldetails)
                    val fm: FragmentManager = supportFragmentManager
                    val dateStart ="30-3-2023"
                    val dateEnd = "1-4-2023"
                    val detailRoom= arrayListOf<Int>(1,2,0)
                    val bundle = Bundle()
                    bundle.putSerializable("BookingDetail", hoteldetails)
                    bundle.putString("dateStart", dateStart)
                    bundle.putString("dateEnd", dateEnd)
                    bundle.putIntegerArrayList("detailRoom",detailRoom)
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
                    bookingBtn.setOnClickListener(){
                        val intent = Intent(this, RoomActivity::class.java)
                        intent.putExtra("hotel_id",hoteldetails?.id);
                        startActivity(intent)
                    }
                } else {

                }
            }
            .addOnFailureListener { exception ->
            }

    }

}