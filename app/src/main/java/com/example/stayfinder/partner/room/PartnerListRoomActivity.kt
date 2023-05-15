package com.example.stayfinder.partner.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var propertyLV: RecyclerView
    private var db: FirebaseFirestore? = null
    private var collectionName :String? = null
    var uuidHotel: String? = null
    var roomList:ArrayList<RoomDetailModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_list_room)
        initActionBar()
        collectionName = "rooms"

        db = Firebase.firestore

        uuidHotel = intent.getStringExtra("uuidHotel")

        requestListRoom(uuidHotel!!)
    }

    private fun requestListRoom(uuidHotel: String){
        db!!.collection(collectionName!!).whereEqualTo("hotel_id", uuidHotel)
            .get()
            .addOnSuccessListener {
                documents ->
                for(document in documents){
                    val room = document.toObject(RoomDetailModel::class.java)
                    roomList.add(room)
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

    private fun deleteRoom(id_room: String) {

    }

    private fun initLV(propertyList:ArrayList<RoomDetailModel>) {
        propertyLV = findViewById(R.id.roomPartnerRV)
        propertyLV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val propertyAdapter = RoomAdapter(this, propertyList)


        propertyAdapter.onEditBtnClick = {pos ->
            val itemIdRoom = roomList[pos].id
            val itemIdHotel = roomList[pos].hotel_id
            val intent = Intent(this, RoomAddHotelDetailActivity::class.java)
            intent.putExtra("uuidRoom", itemIdRoom)
            intent.putExtra("uuidHotel", itemIdHotel)

            startActivity(intent)
        }

        propertyAdapter.onDeleteBtnClick = {pos ->
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Room Delete")
            builder.setMessage("Are you sure you want to delete ${roomList[pos].name}?")

            builder.setPositiveButton("Yes") { dialog, which ->
                db?.collection("rooms")
                    ?.document(roomList[pos].id)
                    ?.delete()
                    ?.addOnSuccessListener {
                        roomList.removeAt(pos)
                        propertyAdapter.notifyItemRemoved(pos)
                        Toast.makeText(this, "Delete room successfully", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    ?.addOnFailureListener { e ->
                        Toast.makeText(this, "Fail to delete room: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        propertyLV.adapter = propertyAdapter



        var addBtn = findViewById<Button>(R.id.addPropertyBtn)
        addBtn.setOnClickListener {
            val intent = Intent(this, RoomAddHotelDetailActivity::class.java)
            intent.putExtra("uuidHotel", uuidHotel)
            startActivity(intent)
        }
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Room Details"
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