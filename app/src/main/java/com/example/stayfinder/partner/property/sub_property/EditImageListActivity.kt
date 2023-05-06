package com.example.stayfinder.partner.property.sub_property

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.stayfinder.R
import com.example.stayfinder.hotels
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditImageListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image_list)
        initActionBar()
        val documents = Firebase.firestore.collection("Hotels")
            .document("5l5PibkyeRaZRFCVPrlB")
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                val l = document.toObject(hotels::class.java)
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