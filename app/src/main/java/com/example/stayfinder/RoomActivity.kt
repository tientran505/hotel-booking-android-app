package com.example.stayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL

class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        val Roomlist = arrayListOf<Room>(
            Room(123,345,room_type.double,"2 3 con mực, anh iu em cực",
            arrayListOf(URL("https://i.pinimg.com/736x/50/d8/62/50d862e0e5d9b35882749cc1b53a03ef.jpg"),URL("https://i.pinimg.com/564x/5f/8d/6a/5f8d6a6a489b521364504cac30803d49.jpg")),"12-12-1001",45.2,20.3,0.5,"Giá cho 2 đêm từ ngày ... đến ngày ...",2)
            ,      Room(123,345,room_type.double,"2 3 con mực, anh iu em cực",
            arrayListOf(URL("https://i.pinimg.com/736x/50/d8/62/50d862e0e5d9b35882749cc1b53a03ef.jpg"),URL("https://i.pinimg.com/564x/5f/8d/6a/5f8d6a6a489b521364504cac30803d49.jpg")),"12-12-1001",45.2,20.3,0.5,"Giá cho 2 đêm từ ngày ... đến ngày ...",2)

        )
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = RoomAdapter(Roomlist)
        recyclerview?.adapter = adapter
    }
}