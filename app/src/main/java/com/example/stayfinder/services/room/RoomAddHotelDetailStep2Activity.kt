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
import java.util.*

class RoomAddHotelDetailStep2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail_step2)

        var room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?
        var editMode = intent.getBooleanExtra("editMode", false)
        var timestamp = intent.getLongExtra("timestamp", 1683536679)


        if(room == null){
            Toast.makeText(this,"Having error when create room", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, PartnerMainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        var calendar = findViewById<CalendarView>(R.id.calendarView)
        var priceEt = findViewById<EditText>(R.id.priceET)
        var nextBtn = findViewById<Button>(R.id.nextBtn)


        if(editMode == true){ // autofill
            calendar.date = timestamp * 1000
            priceEt.setText( room?.origin_price.toString())
        }

        calendar.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->

            val calenderTemp: Calendar = Calendar.getInstance()
            calenderTemp.set(year, month, dayOfMonth)
            calView.setDate(calenderTemp.timeInMillis, true, true)
        }

        nextBtn.setOnClickListener {
            timestamp = calendar.date/1000 // bước cuối mới thêm vào object để truyền đi không bị lỗi
            var price = if( priceEt.text.toString().isNotEmpty()) priceEt.text.toString().toDouble() else null
            room?.origin_price = price

            var intent = Intent(this, RoomAddHotelDetailStep3Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("timestamp", timestamp)
            startActivity(intent)
        }


    }
}