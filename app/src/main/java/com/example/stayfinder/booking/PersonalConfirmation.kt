package com.example.stayfinder.booking

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.stayfinder.R

class PersonalConfirmation : AppCompatActivity() {
    private lateinit var nameET: EditText
    private lateinit var emailET: EditText
    private lateinit var phoneET: EditText
    private lateinit var tripTypeRB: RadioGroup
    private lateinit var workRB: RadioButton
    private lateinit var leisureRB: RadioButton
    private lateinit var originalPriceTV: TextView
    private lateinit var discountPriceTV: TextView

    private lateinit var nextBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_confirmation)

        initActionBar()
        initComponent()

        nextBtn.setOnClickListener {
            val intent = Intent(this, BookingConfirmation::class.java)
            startActivity(intent)
        }
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Your Personal Information"
    }

    private fun initComponent() {
        nameET = findViewById(R.id.nameET)
        emailET = findViewById(R.id.emailET)
        phoneET = findViewById(R.id.phoneET)
        tripTypeRB = findViewById(R.id.tripTypeRB)
        workRB = findViewById(R.id.workRB)
        leisureRB = findViewById(R.id.leisureRB)
        originalPriceTV = findViewById(R.id.oPriceTV)
        discountPriceTV = findViewById(R.id.dcPriceTV)

        originalPriceTV.paintFlags = originalPriceTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        tripTypeRB.check(workRB.id)

        nextBtn = findViewById(R.id.nextBtn)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}