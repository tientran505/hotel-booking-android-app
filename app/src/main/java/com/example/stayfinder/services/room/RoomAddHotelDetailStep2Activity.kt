package com.example.stayfinder.services.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity

class RoomAddHotelDetailStep2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail_step2)

        var room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?

        if(room == null){
            Toast.makeText(this,"Having error when create room", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, PartnerMainActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}