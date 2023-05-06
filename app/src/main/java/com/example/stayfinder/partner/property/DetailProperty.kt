package com.example.stayfinder.partner.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.sub_property.EditImageListActivity
import com.example.stayfinder.partner.property.sub_property.EditLocationActivity

class DetailProperty : AppCompatActivity() {
    private val hotel_id="5l5PibkyeRaZRFCVPrlB"
    lateinit var locationBtn: Button
    lateinit var photoBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_detail_property)
        initActionBar()
        locationBtn = findViewById(R.id.locationBtn)
        photoBtn = findViewById(R.id.photoBtn)
        initActionBar()

        locationBtn.setOnClickListener {
            val intent = Intent(this, EditLocationActivity::class.java)
            intent.putExtra("hotel_id",hotel_id);
            startActivity(intent)
        }
        photoBtn.setOnClickListener {
            val intent = Intent(this, EditImageListActivity::class.java)
            intent.putExtra("hotel_id",hotel_id);
            startActivity(intent)
        }
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