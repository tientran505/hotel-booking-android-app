package com.example.stayfinder.partner.property.sub_property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.adapter.ShowListRoom
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditImageListActivity : AppCompatActivity() {
    private var NameList: ArrayList<String?> = arrayListOf<String?>()
    private var IdList: ArrayList<String?> = arrayListOf<String?>()
    lateinit var recyclerView : RecyclerView
    lateinit var hotel_id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image_list)
        initActionBar()
        recyclerView= findViewById(R.id.recyclerView)
//        println("hotel_id"+hotel_id)
        hotel_id=intent.getStringExtra("hotel_id")!!
        val documents = Firebase.firestore.collection("hotels").document(hotel_id)
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                IdList.add(document.getString("id"))
                NameList.add(document.getString("name"))
                val roomref = Firebase.firestore.collection("rooms").whereEqualTo("hotel_id",hotel_id).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            for (i in documents) {
                                IdList.add(i.getString("id"))
                                NameList.add( i.getString("name"))
                            }
                        }
                        println(NameList)
                        println(IdList)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        val adapter  =  ShowListRoom(NameList)
                        recyclerView.adapter= adapter
                        adapter.onItemClick ={
                            position ->
                            run {
                                val intent = Intent(this, EditImagePage::class.java)
                                println(IdList)
                                intent.putExtra("id", IdList[position])
                                if (position == 0)
                                    intent.putExtra("collection", "hotels")
                                else
                                    intent.putExtra("collection", "rooms")

                                startActivity(intent)
                            }
                        }
                    }
            }
        }

    }


    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Edit Image"
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