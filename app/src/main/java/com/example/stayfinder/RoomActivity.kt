package com.example.stayfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.booking.PersonalConfirmation
import java.net.URL
data class room(
    val id: String = "",
    val hotel_id: String = "",
    val roomtype: String="",
    val description: String ="",
    val photoUrl: ArrayList<String>,
    var available_start_date: String ="",
    var origin_price: Double =0.0,
    var discount_price: Double =0.0,
    var percentage_discount: Double = 0.0,
    var applied_coupon: Int = 0,
    ):java.io.Serializable{
}
class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        val Roomlist = arrayListOf<Room>(
            Room("123","345","double","2 3 con mực, anh iu em cực",
            arrayListOf(("https://i.pinimg.com/736x/50/d8/62/50d862e0e5d9b35882749cc1b53a03ef.jpg"),("https://i.pinimg.com/564x/5f/8d/6a/5f8d6a6a489b521364504cac30803d49.jpg")),"12-12-1001",45.2,20.3,0.5,"Giá cho 2 đêm từ ngày ... đến ngày ...",2)
            ,     Room("123","345","double","2 3 con mực, anh iu em cực",
                arrayListOf(("https://i.pinimg.com/736x/50/d8/62/50d862e0e5d9b35882749cc1b53a03ef.jpg"),("https://i.pinimg.com/564x/5f/8d/6a/5f8d6a6a489b521364504cac30803d49.jpg")),"12-12-1001",45.2,20.3,0.5,"Giá cho 2 đêm từ ngày ... đến ngày ...",2)
        )
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = RoomAdapter(Roomlist)

        recyclerview?.adapter = adapter

        adapter.onButtonClick = { pos ->
            startActivity(Intent(this, PersonalConfirmation::class.java))
        }
    }
}