package com.example.stayfinder.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.*
import com.example.stayfinder.user.User
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class RatingActivity : AppCompatActivity() {
    var submitbtn : Button? = null
    val db = Firebase.firestore

    private fun addReview(review:Review, id: String){
        db.collection("reviews").document(id).set(review)
            .addOnFailureListener {  }
            .addOnSuccessListener {  }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_hotel)

        var itemList = arrayListOf<HotelDetail>()

        itemList.add(HotelDetail("bloblo","Căn nhà mơ ước", "Vũng Tàu"
            , "King Room", 1000000.0, 0.5,
            "Very good", 8.0, 500, R.drawable.purpl))

        val lists = findViewById<RecyclerView>(R.id.hotelinfo) as RecyclerView
        var adapter = DetailListAdapter(itemList)
        lists.adapter = adapter
        lists.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener((object : DetailListAdapter.onItemClickListener{
            override fun onItemClick(position: Int){

            }
        }))

        submitbtn = findViewById(R.id.submit)

        submitbtn!!.setOnClickListener{
            val cleanlinessrtb : RatingBar = findViewById(R.id.cleanliness)
            val comfortrtb : RatingBar = findViewById(R.id.comfort)
            val servicesrtb : RatingBar = findViewById(R.id.services)
            val locationrtb : RatingBar = findViewById(R.id.location)

            val cleanliness = cleanlinessrtb.rating.toDouble()
            val comfort = comfortrtb.rating.toDouble()
            val services = servicesrtb.rating.toDouble()
            val location = locationrtb.rating.toDouble()

            val rating : rating = rating(cleanliness,comfort,services,location)
            val rating_overall = (cleanliness + comfort + services + location)/4.0
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val review_date = LocalDate.now().format(formatter)

            val id = db.collection("reviews").document().id
            val userReview :Review = Review(id,"1",itemList[0].id!!,review_date,rating,rating_overall)
            addReview(userReview,id)
        }
    }
}