package com.example.stayfinder.partner.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.partner.property.adapter.Property
import com.example.stayfinder.partner.room.PartnerListRoomActivity
import com.example.stayfinder.services.room.RoomAddHotelDetailActivity

class DetailProperty : AppCompatActivity() {

    lateinit var uuidHotel:String
    lateinit var hotel:Property
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_detail_property)

        uuidHotel = intent.getStringExtra("uuidHotel")!!
        hotel = intent.getSerializableExtra("hotel") as Property

        initActionBar()

        findViewById<TextView>(R.id.propertyNameDetail).setText(hotel.propertyName)
        findViewById<TextView>(R.id.propertyAddressDetail).setText(hotel.address)

        val img : ImageView = findViewById(R.id.imgPropertyDetail)
        img.setImageResource(R.drawable.img_1)

        Glide.with(this)
            .load(hotel.imgUrl)
            .centerCrop()
            .into(img)

        findViewById<Button>(R.id.roomBtn).setOnClickListener {
            var intent = Intent(this, PartnerListRoomActivity::class.java)
            intent.putExtra("uuidHotel",uuidHotel)
            startActivity(intent)
        }

    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Property Detail"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}