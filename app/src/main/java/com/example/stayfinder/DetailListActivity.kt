package com.example.stayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetailListActivity : AppCompatActivity() {

    var itemList = arrayListOf<HotelDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list)

        itemList.add(HotelDetail("bloblo","Căn nhà mơ ước", "Vũng Tàu"
            , "King Room", 1000000.0, 0.5,
            "Very good", 8.0, 500, R.drawable.purpl))

        val lists = findViewById<RecyclerView>(R.id.recyclerDetail) as RecyclerView
        var adapter = DetailListAdapter(itemList)
        lists.adapter = adapter
        lists.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener((object : DetailListAdapter.onItemClickListener{
            override fun onItemClick(position: Int){

            }
        }))

    }
}