package com.example.stayfinder.hotel.hotel_detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.*

class HotelDetailActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail2)
        val bundle = intent.extras
        val fragment_type = bundle!!.getString("fragment_type")
        val hotel_id = bundle.getString("hotel_id")
        println("booking_id"+ hotel_id)
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
        }
        when(fragment_type){
            "feedback" ->{
                println("hotel_id 1233 "+hotel_id)
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
                        val fragInfo1 = HotelDetailImageDirect()
                        val bundle2 = Bundle()
                        bundle2.putInt("position",bundle!!.getInt("position"))
                        bundle2.putSerializable("list", bundle!!.getSerializable("list"))
                        fragInfo1.setArguments(bundle2);
                        fm.beginTransaction().replace(R.id.frameLayout, fragInfo1).commit();
                    }
                    "grid"->{
                        val fm: FragmentManager = supportFragmentManager
                        val fragInfo1 = SubBookingDetailImageList()
                        fragInfo1.setArguments(bundle);
                        val bundle2 = Bundle()
                        bundle2.putSerializable("booking_id",booking_id)
                        fragInfo1.setArguments(bundle2);
                        fm.beginTransaction().replace(R.id.frameLayout, fragInfo1).commit();

                    }
            }
            "image"->{
                db.collection("hotels").whereEqualTo("id", hotel_id)
                    .get()
                    .addOnSuccessListener { hotelDocuments ->
                        // Iterate through each hotel document and add its image URLs to the list
                        for (hotelDocument in hotelDocuments) {
                            val hotelImageUrlList = hotelDocument.get("photoUrl") as List<String>
                            println("hotelImageUrlList"+hotelImageUrlList)
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
                                        println("roomImageUrlList"+roomImageUrlList)

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