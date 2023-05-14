package com.example.stayfinder.partner.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ListView
import com.example.stayfinder.R
import com.example.stayfinder.hotels
import com.example.stayfinder.partner.property.adapter.PropertyAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PartnerHotelList : AppCompatActivity() {
    private var collectionName :String? = null

    private lateinit var propertyLV: ListView
    private val propertyList = ArrayList<hotels>()

    val db = Firebase.firestore

    private lateinit var propertyAdapter: PropertyAdapter

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

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Manage Promotion"
    }
    private fun fetchData() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val docRef = db.collection("hotels").whereEqualTo("owner_id", user.uid).whereNotEqualTo("hotel_name", "")
            docRef.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val hotel = document.toObject<hotels>()
                    Log.d("tien", hotel.toString())
                    propertyList.add(hotel)
                }
                propertyAdapter.notifyDataSetChanged()
            }
                .addOnFailureListener { exception ->
                    Log.w("log", "Error getting documents: ", exception)
                }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_hotel_list)

        initActionBar()
        fetchData()

        propertyLV = findViewById(R.id.propertyPartnerLV)
        propertyAdapter = PropertyAdapter(this,propertyList)

        propertyLV.adapter = propertyAdapter
        propertyLV.setOnItemClickListener { adapterView, view, i, l ->
            var itemIdHotel = propertyList[i].id
            val intent = Intent(this, PartnerCouponList::class.java)
            intent.putExtra("hotel_id", itemIdHotel)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}