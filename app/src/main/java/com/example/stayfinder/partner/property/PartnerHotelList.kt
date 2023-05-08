package com.example.stayfinder.partner.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.adapter.Property
import com.example.stayfinder.partner.property.adapter.PropertyAdapter

class PartnerHotelList : AppCompatActivity() {
    private lateinit var propertyLV: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_hotel_list)

        propertyLV = findViewById(R.id.propertyPartnerLV)

        val urlStr = "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80"

        val propertyList = listOf<Property>(
            Property(urlStr, "Property 1"),
            Property(urlStr, "Property 2"),
            Property(urlStr, "Property 3"),
            Property(urlStr, "Property 4"),
            Property(urlStr, "Property 5"),
            Property(urlStr, "Property 6"),
            Property(urlStr, "Property 7"),
            Property(urlStr, "Property 8"),
            Property(urlStr, "Property 9"),
            Property(urlStr, "Property 10"),
            Property(urlStr, "Property 11"),

            )

        val propertyAdapter = PropertyAdapter(this, propertyList)
        propertyLV.adapter = propertyAdapter
        propertyLV.setOnItemClickListener { adapterView, view, i, l ->
            startActivity(Intent(this, DetailProperty::class.java))
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}