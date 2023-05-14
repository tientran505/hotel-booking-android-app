package com.example.stayfinder.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.*
import com.example.stayfinder.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
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

    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private fun addReview(review:Review, id: String){
        db.collection("reviews").document(id).set(review)
            .addOnFailureListener {  }
            .addOnSuccessListener {  }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_hotel)

        var itemList = arrayListOf<HotelDetail>()

        itemList.add(HotelDetail("bloblo","Căn nhà mơ ước", 1000000000.0
            , "King Room"
            , "https://firebasestorage.googleapis.com/v0/b/hotel-booking-app-b6d5b.appspot.com/o/images%2Fa9b80ff2-aeb6-4e98-906c-78b0932d5dd0?alt=media&token=bdea8bd6-0cb7-4de9-9e24-4080a82793da"))

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
                ,itemList[0].id!!,review_date,rating,rating_overall,title,content)
            addReview(userReview,id)
        }
    }
}