package com.example.stayfinder.hotel.hotel_detail

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.*
import com.example.stayfinder.hotel.hotel_detailimport.SubBookingDetailImageList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HotelDetailActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hotel_detail2)

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
                val fm: FragmentManager = supportFragmentManager
                val fragFeedback = HotelDetailFeedBack()
                val bundle2 = Bundle()
                bundle2.putString("booking_id",hotel_id)
                fragFeedback.setArguments(bundle2);
                fm.beginTransaction().replace(R.id.frameLayout, fragFeedback).commit();
            }
            "image"->{
                val imageUrls = ArrayList<String>()
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
                                val listImageFragment = SubBookingDetailImageList()
                                val bundle2 = Bundle()
                                bundle2.putSerializable("listImage", imageUrls)
                                listImageFragment.setArguments(bundle2);
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, listImageFragment)
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