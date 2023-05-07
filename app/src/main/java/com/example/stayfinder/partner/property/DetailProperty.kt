package com.example.stayfinder.partner.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.example.stayfinder.R
import com.example.stayfinder.services.room.RoomAddHotelDetailActivity

class DetailProperty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_detail_property)

        initActionBar()

        findViewById<Button>(R.id.roomBtn).setOnClickListener {
            var intent = Intent(this, RoomAddHotelDetailActivity::class.java)
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