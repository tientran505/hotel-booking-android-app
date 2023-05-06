package com.example.stayfinder.services.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.firebase.Timestamp

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

        var calendar = findViewById<CalendarView>(R.id.calendarView)
        var priceEt = findViewById<EditText>(R.id.priceET)
        var nextBtn = findViewById<Button>(R.id.nextBtn)

        nextBtn.setOnClickListener {
            var timestamp = calendar.date/1000 // bước cuối mới thêm vào object để truyền đi không bị lỗi
            var price = if( priceEt.text.toString().isNotEmpty()) priceEt.text.toString().toDouble() else null
            room?.origin_price = price

            var intent = Intent(this, RoomAddHotelDetailStep3Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("timestamp", timestamp.toString())
            startActivity(intent)
        }


    }
}