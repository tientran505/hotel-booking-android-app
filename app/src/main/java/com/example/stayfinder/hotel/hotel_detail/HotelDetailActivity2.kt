package com.example.stayfinder.hotel.hotel_detail
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.R
import com.example.stayfinder.Review
import com.example.stayfinder.RoomActivity
import com.example.stayfinder.rating
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class HotelDetailActivity2 : AppCompatActivity() {
    private val imageUrls = ArrayList<String>()
    private val reviewsData = ArrayList<Review>()
    val db = Firebase.firestore
    private val ratingData = rating()
    lateinit var progressBar: ProgressBar
    var title: String =""
    fun getImages(): ArrayList<String> {
        return imageUrls
    }
    fun getReview(): ArrayList<Review> {
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

        val dateStart: String = "30-3-2023"
        val dateEnd: String = "1-4-2023"
        val detailRoom = arrayListOf<Int>(1, 2, 0)
        bookingBtn.setOnClickListener() {
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra("hotel_id", hotel_id);
            intent.putExtra("dateStart", dateStart)
            intent.putExtra("dateEnd", dateEnd)
            intent.putIntegerArrayListExtra("detailRoom", detailRoom)
            startActivity(intent)
        }
        if (fragment_type != null) {
            this.title = fragment_type
        }
        initActionBar()
        when(fragment_type){
            "feedback" ->{
                println("hotel_id 1233"+hotel_id)
                db.collection("reviews").whereEqualTo("hotel_id", hotel_id)
                    .get()
                    .addOnSuccessListener { reviews ->
                        for (review in reviews) {
                            val review = review.toObject(Review::class.java)
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
    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
//        menu?.title = "Booking confirmation" // title of activity
        if(this.title =="feedback") menu?.title = "Hotel Reviews"
        if(this.title =="image") menu?.title = "Hotel Images"

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