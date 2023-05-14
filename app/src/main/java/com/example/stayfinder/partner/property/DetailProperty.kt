package com.example.stayfinder.partner.property

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView

import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
//import com.example.stayfinder.partner.property.adapter.Property
import com.example.stayfinder.partner.room.PartnerListRoomActivity
import android.widget.ProgressBar
import com.example.stayfinder.Property

import com.example.stayfinder.hotels
import com.example.stayfinder.services.room.RoomAddHotelDetailActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DetailProperty : AppCompatActivity() {


    lateinit var uuidHotel:String
//    lateinit var hotel: Property
    private lateinit var imgProperty: ImageView
    private lateinit var propertyName: TextView
    private lateinit var propertyAddress: TextView

    private lateinit var hotel: hotels

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_detail_property)

        uuidHotel = intent.getStringExtra("uuidHotel")!!
//        hotel = intent.getSerializableExtra("hotel") as Property
        hotel = intent.getSerializableExtra("hotel") as hotels


        val intent = intent
//        hotel = intent.getSerializableExtra("vwProperty", hotels::class.java) as hotels

        val hotelId = intent.getStringExtra("vwProperty")
        initComponent()


        CoroutineScope(Dispatchers.Main).launch {
//            hotel = hotelId?.let { getHotelById(it) }!!
            assignComponent()
        }


        initActionBar()

//        findViewById<TextView>(R.id.propertyNameDetail).setText(hotel.name)
//        findViewById<TextView>(R.id.propertyAddressDetail).setText(hotel.address)

        assignComponent()


//        val img : ImageView = findViewById(R.id.imgPropertyDetail)
//        img.setImageResource(R.drawable.img_1)
//
//        Glide.with(this)
//            .load(hotel.imgUrl)
//            .centerCrop()
//            .into(img)

        findViewById<Button>(R.id.roomBtn).setOnClickListener {
            val intent = Intent(this, PartnerListRoomActivity::class.java)
            intent.putExtra("uuidHotel",uuidHotel)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private suspend fun getHotelById(id: String): hotels? {
        val db = Firebase.firestore

        return withContext(Dispatchers.IO) {
            try {
                val docRef = db.collection("hotels").document(id)
                val document = docRef.get().await()

                document.toObject(hotels::class.java)
            } catch (e: Exception) {
                null
            }
        }

    }

    private fun initComponent() {
        imgProperty = findViewById(R.id.imgPropertyDetail)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility  = View.VISIBLE
        propertyName = findViewById(R.id.propertyNameDetail)
        propertyAddress = findViewById(R.id.propertyAddressDetail)
    }

    private fun assignComponent() {
        var urlPhoto = ""
        urlPhoto = if (hotel.photoUrl.size > 0) {
            hotel.photoUrl[0]
        } else {
            "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80"
        }

        Glide.with(this)
            .load(urlPhoto)
            .centerCrop()
            .into(imgProperty)

        propertyName.text = hotel.name
        val address = hotel.address
        propertyAddress.text = address.address

        progressBar.visibility = View.GONE
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