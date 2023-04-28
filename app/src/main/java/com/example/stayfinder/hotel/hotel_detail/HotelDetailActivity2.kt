package com.example.stayfinder.hotel.hotel_detail
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.R
import com.example.stayfinder.RoomActivity
import com.example.stayfinder.rating
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class reviewss(
    var id: String = "",
    var user: user = user(),
    var hotel_id:String = "",
    var review_date:String = "",
    var title: String="",
    var content: String="",
    var rating: rating = rating(),
    var rating_overall:Double = 0.0,
)
data class user(
    var user_id: String ="",
    var display_name: String = "",
    var photoUrl: String = "",
)
class HotelDetailActivity2 : AppCompatActivity() {
    private val imageUrls = ArrayList<String>()
    private val reviewsData = ArrayList<reviewss>()
    val db = Firebase.firestore
    private val ratingData = rating()
    lateinit var progressBar: ProgressBar

    fun getImages(): ArrayList<String> {
        return imageUrls
    }
    fun getReview(): ArrayList<reviewss> {
        return reviewsData
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hotel_detail2)
        progressBar = findViewById(R.id.savedListPB)
        val bundle = intent.extras
        val fragment_type = bundle!!.getString("fragment_type")
        val hotel_id = bundle!!.getString("booking_id")
        val bookingBtn = findViewById<Button>(R.id.BookingBtn)
        val db = Firebase.firestore

        bookingBtn.setOnClickListener(){
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra("hotel_id",hotel_id);
            startActivity(intent)
        }
        when(fragment_type){
            "feebback" ->{
                println("hotel_id 1233"+hotel_id)
                db.collection("reviews").whereEqualTo("hotel_id", hotel_id)
                    .get()
                    .addOnSuccessListener { reviews ->
                        for (review in reviews) {
                            val review = review.toObject(reviewss::class.java)
                            println("review +"+review)
                            reviewsData.add(review)
                        }
                        progressBar.visibility = View.GONE

                        val fm: FragmentManager = supportFragmentManager
                        val fragFeedback = HotelDetailFeedBack()
                        val bundle2 = Bundle()
                        bundle2.putString("booking_id",hotel_id)
                        bundle2.putSerializable("rating",bundle!!.getSerializable("rating"))
                        fragFeedback.setArguments(bundle2);
                        fm.beginTransaction().replace(R.id.frameLayout, fragFeedback).commit();
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this,"Cannot load Image, please try again later", Toast.LENGTH_SHORT).show()
                        finish();
                    }
            }
            "image"->{
                db.collection("Hotels").whereEqualTo("id", hotel_id)
                    .get()
                    .addOnSuccessListener { hotelDocuments ->
                        // Iterate through each hotel document and add its image URLs to the list
                        for (hotelDocument in hotelDocuments) {
                            val hotelImageUrlList = hotelDocument.get("photoUrl") as List<String>
                            imageUrls.addAll(hotelImageUrlList)
                        }
                        db.collection("rooms")
                            .get()
                            .addOnSuccessListener { roomDocuments ->
                                // Iterate through each room document and add its image URLs to the list
                                for (roomDocument in roomDocuments) {
                                    val roomId = roomDocument.get("hotel_id") as String
                                    if (hotelDocuments.any { it.id == roomId }) {
                                        val roomImageUrlList = roomDocument.get("photoUrl") as List<String>
                                        imageUrls.addAll(roomImageUrlList)
                                    }
                                }
                                progressBar.visibility = View.GONE
                                val listImageFragment = SubBookingDetailImageList()
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, listImageFragment)
                                    .commit()

                            }
                            .addOnFailureListener { exception ->
                                // Handle any errors here
                                Toast.makeText(this,"Cannot load Image, please try again later", Toast.LENGTH_SHORT).show()
                                finish();
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors here
                        Toast.makeText(this,"Cannot load Image, please try again later", Toast.LENGTH_SHORT).show()
                        finish();
                    }

            }
        }

    }
}