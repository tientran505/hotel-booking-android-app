package com.example.stayfinder.partner.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.*
import com.example.stayfinder.partner.property.adapter.CouponInfoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PartnerCouponList : AppCompatActivity() {

    var couponList = arrayListOf<coupon>()
    val db = Firebase.firestore
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    lateinit var listadapter:CouponInfoAdapter
    private suspend fun loadSavedLists() {
        val documents = Firebase.firestore.collection("coupons")
            .whereEqualTo("owner_id", "blabla")
            .get()
            .await()
        for (document in documents) {
            val l = document.toObject(coupon::class.java)
            couponList.add(l)
            listadapter.notifyItemInserted(couponList.size - 1)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_coupon_list)

        CoroutineScope(MainScope().coroutineContext).launch {
            loadSavedLists()
        }

        val cpList = findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        listadapter = CouponInfoAdapter(couponList)
        cpList.adapter = listadapter
        cpList.layoutManager = LinearLayoutManager(this)

        listadapter.setOnIconClickListener((object:CouponInfoAdapter.onIconClickListener{
            override fun onIconClick(position: Int) {
                TODO("Not yet implemented")
            }

        }))

        listadapter.setOnItemClickListener((object : CouponInfoAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                TODO("Not yet implemented")
            }

        }))

        var addbtn = findViewById<Button>(R.id.button)
        addbtn.setOnClickListener{
            val intent = Intent(this, PartnerAddCoupon::class.java)
            startActivity(intent)
        }
    }
}