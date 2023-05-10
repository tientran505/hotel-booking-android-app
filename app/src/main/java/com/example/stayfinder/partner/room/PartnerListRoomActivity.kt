package com.example.stayfinder.partner.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.example.stayfinder.partner.room.adapter.ListRoomModel
import com.example.stayfinder.partner.room.adapter.RoomAdapter
import com.example.stayfinder.services.room.RoomAddHotelDetailActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PartnerListRoomActivity : AppCompatActivity() {

    private lateinit var propertyLV: ListView
    private var db: FirebaseFirestore? = null
    private var collectionName :String? = null
    var uuidHotel: String? = null
    var roomList:ArrayList<ListRoomModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_list_room)
        collectionName = "TestRoom"

        db = Firebase.firestore

        uuidHotel = intent.getStringExtra("uuidHotel")
        //uuidHotel = "ddddddddd"

        requestListRoom(uuidHotel!!)

    }

    private fun requestListRoom(uuidHotel: String){

        val urlStr = "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80"

        db!!.collection(collectionName!!).whereEqualTo("hotelId", uuidHotel)
            .get()
            .addOnSuccessListener {
                documents ->
                for(document in documents){
                    val room = document.toObject(RoomDetailModel::class.java)
                    var roomModel = ListRoomModel(uuidHotel = uuidHotel, uuidRoom = room.id, typeRoom = room.room_type, urlImage = if(room.photoUrl.size > 0) room.photoUrl[0] else urlStr)
                    roomList.add(roomModel)
                }
                initLV(roomList)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Have error, please try again", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PartnerMainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }


    }

    private fun initLV(propertyList:ArrayList<ListRoomModel>) {
        propertyLV = findViewById(R.id.roomPartnerLV)

        val propertyAdapter = RoomAdapter(this, propertyList)

        propertyLV.adapter = propertyAdapter

        propertyLV.setOnItemClickListener { adapterView, view, i, l ->
            var itemIdRoom = roomList[i].uuidRoom
            var itemIdHotel = roomList[i].uuidHotel
            val intent = Intent(this, RoomAddHotelDetailActivity::class.java)
            intent.putExtra("uuidRoom", itemIdRoom)
            intent.putExtra("uuidHotel", itemIdHotel)

            startActivity(intent)
        }

        var addBtn = findViewById<Button>(R.id.addPropertyBtn)
        addBtn.setOnClickListener {
            val intent = Intent(this, RoomAddHotelDetailActivity::class.java)
            intent.putExtra("uuidHotel", uuidHotel)
            startActivity(intent)
        }
    }
}