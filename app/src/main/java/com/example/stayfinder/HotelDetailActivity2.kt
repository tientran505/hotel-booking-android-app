package com.example.stayfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class HotelDetailActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail2)
        val bundle = intent.extras
        val fragment_type = bundle!!.getString("fragment_type")
        val booking_id = bundle!!.getString("booking_id")
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

                        val position = bundle!!.getInt("position")
                        println("list position"+ position.toString())
                        val fm: FragmentManager = supportFragmentManager
                        val fragInfo1 = HotelDetailImageDirect()
                        val bundle2 = Bundle()
                        bundle2.putInt("position",position)
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