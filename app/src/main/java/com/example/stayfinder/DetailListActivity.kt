package com.example.stayfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.hotel.hotel_detail.HotelDetailActivity
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.property.PartnerCouponList
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.DecimalFormat

class DetailListActivity : AppCompatActivity() {

    lateinit var listadapter:DetailListAdapter
    var itemList = arrayListOf<saved_list_item>()
    var hotelList = arrayListOf<HotelDetail>()
    val db = Firebase.firestore
    var num_of_items:Int = 0
    var list_id:String? = ""

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

    private fun loadSavedLists(list_id:String){
        val documents = db.collection("saved_list_items")
            .whereEqualTo("list_id", list_id)
            .orderBy("create_date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener{documents ->
                for(document in documents){
                    //Log.d("test", "${document.id} => ${document.data}")
                    var hotel = document.toObject(saved_list_item::class.java)
                    itemList.add(hotel)
                    val room_documents = db.collection("rooms")
                                        .whereEqualTo("hotel_id",hotel.hotel_id)
                                        .orderBy("discount_price").limit(1).get()
                                        .addOnSuccessListener {room_documents ->
                                            Log.d("test", "${room_documents.documents} => ${room_documents.size()}")
                                            if (!room_documents.isEmpty){
                                                var room = room_documents.documents[0].toObject<RoomDetailModel>()
                                                hotelList.add(HotelDetail(hotel.hotel_id, hotel.titlename,
                                                    room!!.discount_price, room!!.room_type,hotel.img))
                                                listadapter.notifyItemInserted(hotelList.size - 1)
                                            }
                                        }
                }
            }
            .addOnFailureListener{
                Log.w("test", "Error getting documents: ")
            }
    }

    private fun deleteItemFromList(pos:Int){
        db.collection("saved_list_items")
            .document(itemList[pos].id)
            .delete()
            .addOnSuccessListener{
                itemList.removeAt(pos)
                hotelList.removeAt(pos)
                listadapter.notifyItemRemoved(pos)
                num_of_items-=1
                db.collection("saved_lists").document(list_id!!).update("number_of_item",num_of_items)
                    .addOnSuccessListener {  }
                    .addOnFailureListener {  }
            }
            .addOnFailureListener {  }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list)

        initActionBar()

        list_id = intent.getStringExtra("list_id")
        var list_name = intent.getStringExtra("list_name")
        num_of_items = intent.getIntExtra("number_item",0)
        loadSavedLists(list_id!!)
        var listname = findViewById<TextView>(R.id.listname)
        listname.setText(list_name)
        var numprops = findViewById<TextView>(R.id.numprops)
        numprops.setText(num_of_items.toString() + " items saved")

        val lists = findViewById<RecyclerView>(R.id.recyclerDetail) as RecyclerView
        listadapter = DetailListAdapter(hotelList)
        lists.adapter = listadapter
        lists.layoutManager = LinearLayoutManager(this)

        listadapter.setOnItemClickListener((object : DetailListAdapter.onItemClickListener{
            override fun onItemClick(position: Int){
                val intent = Intent(this@DetailListActivity,HotelDetailActivity::class.java)
                intent.putExtra("hotel_id",hotelList[position].id)
                startActivity(intent)
            }
        }))

        listadapter.setOnIconClickListener((object:DetailListAdapter.onIconClickListener{
            override fun onIconClick(position: Int) {
                deleteItemFromList(position)
            }
        }))

    }
}