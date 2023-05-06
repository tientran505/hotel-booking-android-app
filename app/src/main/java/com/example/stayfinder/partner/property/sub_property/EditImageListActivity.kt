package com.example.stayfinder.partner.property.sub_property

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.FeedbackAdapter
import com.example.stayfinder.R
import com.example.stayfinder.hotel.hotel_detail.MyGridAdapter
import com.example.stayfinder.hotels
import com.example.stayfinder.partner.property.adapter.EditImage
import com.example.stayfinder.partner.property.adapter.EditImageAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class EditImageListActivity : AppCompatActivity() {
    private var ImageList: ArrayList<EditImage> = arrayListOf<EditImage>()
    val hotel_id ="5l5PibkyeRaZRFCVPrlB"
    lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image_list)
        initActionBar()
        recyclerView= findViewById(R.id.recyclerView)
        val documents = Firebase.firestore.collection("Hotels")
            .document(hotel_id)
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                ImageList.add(
                    EditImage(
                        document.getString("id"),
                        document.getString("hotel_name"),
                        document.get("photoUrl") as ArrayList<String>                            )
                )
                val roomref = Firebase.firestore.collection("rooms").whereEqualTo("hotel_id",hotel_id).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            for (i in documents) {
                                ImageList.add(
                                    EditImage(
                                        i.getString("id"),
                                        i.getString("name"),
                                        i.get("photoUrl") as ArrayList<String>
                                    )
                                )
                            }
                        }
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        val adapter  =  EditImageAdapter(ImageList)
                        recyclerView.adapter= adapter

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