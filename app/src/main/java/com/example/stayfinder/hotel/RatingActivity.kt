package com.example.stayfinder.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stayfinder.*
import com.example.stayfinder.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class RatingActivity : AppCompatActivity() {
    var submitbtn : Button? = null
    val db = Firebase.firestore
    var hotel_id ="eb875113-c692-4219-b78e-59a016c625be"
    lateinit var hotel:hotels


    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

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

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Coupon Management"
    }
    private fun addReview(review:Review, id: String){
        db.collection("reviews").document(id).set(review)
            .addOnFailureListener {  }
            .addOnSuccessListener {  }
    }

    private fun getHotelInfo(callback: () -> Unit){
        db.collection("hotels").document(hotel_id).get()
            .addOnSuccessListener {
                hotel = it.toObject<hotels>()!!
                callback.invoke()
            }
            .addOnFailureListener {  }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_hotel)
        initActionBar()

        getHotelInfo{
            var hotel_name = findViewById<TextView>(R.id.hotelName)
            hotel_name.setText(hotel.name)

            var img = findViewById<ImageView>(R.id.imageView8)
            Glide
                .with(this)
                .load(hotel.photoUrl[0])
                .centerCrop()
                .into(img);

            submitbtn = findViewById(R.id.submit)
            submitbtn!!.setOnClickListener{
                val cleanlinessrtb : RatingBar = findViewById(R.id.cleanliness)
                val comfortrtb : RatingBar = findViewById(R.id.comfort)
                val servicesrtb : RatingBar = findViewById(R.id.services)
                val locationrtb : RatingBar = findViewById(R.id.location)
                val commenttitle : EditText = findViewById(R.id.rvtitle)
                val comment : EditText = findViewById(R.id.comment)

                val cleanliness = cleanlinessrtb.rating.toDouble()
                val comfort = comfortrtb.rating.toDouble()
                val services = servicesrtb.rating.toDouble()
                val location = locationrtb.rating.toDouble()
                val title = commenttitle.text.toString()
                val content = comment.text.toString()

                val rating : rating = rating(cleanliness,comfort,services,location)
                val rating_overall = (cleanliness + comfort + services + location)/4.0
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val review_date = LocalDate.now().format(formatter)

                val id = db.collection("reviews").document().id
                val userReview :Review = Review(id,User_cmt(user?.uid.toString()
                    ,user?.displayName.toString(),user?.photoUrl.toString())
                    ,hotel_id!!,review_date,rating,rating_overall,title,content)
                addReview(userReview,id)
            }
        }
    }
}