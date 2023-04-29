package com.example.stayfinder.user.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.stayfinder.R

class ProfileActivity : AppCompatActivity() {
    lateinit var displaynameET: EditText
    lateinit var nameET: EditText
    lateinit var phoneET: EditText
    lateinit var emailET: EditText
    lateinit var verifyImg: ImageView
    lateinit var avarImg: ImageView
    lateinit var editBtn: Button
    lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initActionBar()

        displaynameET = findViewById(R.id.displaynameET)
        nameET = findViewById(R.id.nameET)
        phoneET = findViewById(R.id.phoneET)
        emailET = findViewById(R.id.emailET)
        verifyImg = findViewById(R.id.emailVerified)
        avarImg = findViewById(R.id.avarImg)
        editBtn = findViewById(R.id.EditBtn)
        saveBtn = findViewById(R.id.SaveBtn)

        saveBtn.visibility = View.GONE
        displaynameET.setEnabled(false);
        nameET.setEnabled(false);
        phoneET.setEnabled(false);
        emailET.setEnabled(false);








    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Booking confirmation" // title of activity
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