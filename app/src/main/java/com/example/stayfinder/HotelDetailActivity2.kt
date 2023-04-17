package com.example.stayfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class HotelDetailActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail2)
        val bundle = intent.extras
        val fragment_type = bundle!!.getString("fragment_type")
        val booking_id = bundle!!.getString("booking_id")
        val bookingBtn = findViewById<Button>(R.id.BookingBtn)
        bookingBtn.setOnClickListener(){
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra("hotel_id",booking_id);
            startActivity(intent)
        }
        when(fragment_type){
            "feebback" ->{
                val fm: FragmentManager = supportFragmentManager
                val fragInfo1 = HotelDetailFeedBack()
                val bundle2 = Bundle()
                bundle2.putString("booking_id",booking_id)
                fragInfo1.setArguments(bundle2);
                fm.beginTransaction().replace(R.id.frameLayout, fragInfo1).commit();
            }
            "image"->{
                val type = bundle!!.getString("type")
                when(type){
                    "url"->{
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

            }
        }

    }
}