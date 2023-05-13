package com.example.stayfinder.partner.property

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.*
import com.example.stayfinder.partner.property.adapter.CouponInfoAdapter
import com.example.stayfinder.partner.property.adapter.coupon_adapter
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
    var couponListAdapter = arrayListOf<coupon_adapter>()
    val db = Firebase.firestore
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    lateinit var listadapter:CouponInfoAdapter

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
        menu?.title = "Coupon Management"
    }
    private suspend fun loadSavedLists(coupon_id: String) {
        val documents = Firebase.firestore.collection("coupons")
            .whereEqualTo("owner_id", user!!.uid)
            .get()
            .await()
        for (document in documents) {
            val l = document.toObject(coupon::class.java)
            lateinit var cp:coupon_adapter
            if(coupon_id == l.id){
                cp = coupon_adapter(l.id,l.title,l.discount,l.startDate,l.endDate,true)
            }
            else{
                cp = coupon_adapter(l.id,l.title,l.discount,l.startDate,l.endDate,false)
            }
            couponListAdapter.add(cp)
            couponList.add(l)
            listadapter.notifyItemInserted(couponList.size - 1)
        }
    }

    private fun changeCoupon(coupon_id: String, hotel_id:String){
        db.collection("hotels").document(hotel_id).update("coupon",coupon_id)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    private fun deleteCoupon(coupon_id: String, pos: Int){
        couponListAdapter.removeAt(pos)
        listadapter.notifyItemRemoved(pos)
        db.collection("coupons").document(coupon_id).delete()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_coupon_list)

        initActionBar()

        var hotel_id = intent.getStringExtra("hotel_id")
        var coupon_id = intent.getStringExtra("coupon_id")

        CoroutineScope(MainScope().coroutineContext).launch {
            loadSavedLists("")
        }

        val cpList = findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        listadapter = CouponInfoAdapter(couponListAdapter)
        cpList.adapter = listadapter
        cpList.layoutManager = LinearLayoutManager(this)

        listadapter.setOnIconClickListener((object:CouponInfoAdapter.onIconClickListener{
            override fun onIconClick(position: Int) {
                var cp_id: String = couponListAdapter[position].id
                deleteCoupon(cp_id, position)
            }

        }))

        val changeDialog = Dialog(this)
        changeDialog.setContentView(R.layout.partner_dialog_change_coupon)

        listadapter.setOnItemClickListener((object : CouponInfoAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                changeDialog.show()
                val okBtn = changeDialog.findViewById<Button>(R.id.okBtn)
                okBtn.setOnClickListener{
                    var cp_id: String = couponListAdapter[position].id
                    changeCoupon(cp_id,hotel_id!!)
                    couponListAdapter[position].status = true
                    if(listadapter.lastSelectedPostion != RecyclerView.NO_POSITION){
                        couponListAdapter[listadapter.lastSelectedPostion].status = false
                    }
                    listadapter.setSelectedPosition(position)
                    changeDialog.dismiss()
                }
            }

        }))

        var addbtn = findViewById<Button>(R.id.button)
        addbtn.setOnClickListener{
            val intent = Intent(this, PartnerAddCoupon::class.java)
            startActivityForResult(intent,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data!!.getSerializableExtra("100")as coupon
                var newcp = coupon_adapter(result.id, result.title, result.discount, result.startDate,
                    result.endDate, false)
                couponListAdapter.add(newcp)
                listadapter.notifyItemInserted(couponListAdapter.size-1)
            }
        }

    }
}