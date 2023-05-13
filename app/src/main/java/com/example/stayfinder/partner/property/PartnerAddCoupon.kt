package com.example.stayfinder.partner.property

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.example.stayfinder.R
import com.example.stayfinder.coupon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class PartnerAddCoupon : AppCompatActivity() {
    val db = Firebase.firestore

    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private fun addCoupon(cp: coupon, id: String){
        db.collection("coupons").document(id).set(cp)
            .addOnFailureListener {  }
            .addOnSuccessListener {  }
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

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
    }

    // Custom class to define min and max for the edit text
    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        // Initialized
        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        // Check if input c is in between min a and max b and
        // returns corresponding boolean
        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity_add_coupon)

        initActionBar()

        var hotel_id = intent.getStringExtra("hotel_id")

        var startDate = findViewById<EditText>(R.id.startDate)
        startDate.isClickable = true
        startDate.isFocusable = false
        startDate.setOnClickListener{
            startDate.setText("")
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    startDate.setText((dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year))
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        var endDate = findViewById<EditText>(R.id.endDate2)
        endDate.isClickable = true
        endDate.isFocusable = false
        endDate.setOnClickListener{
            endDate.setText("")
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    endDate.setText((dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year))
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        var couponValue = findViewById<EditText>(R.id.couponValue)
        couponValue.filters = arrayOf<InputFilter>(MinMaxFilter(1, 99))

        val addbtn = findViewById<Button>(R.id.addbtn)
        addbtn.setOnClickListener{
            val getStartDate = startDate.text.toString()
            val getEndDate = endDate.text.toString()

            var couponName = findViewById<EditText>(R.id.couponName)
            val getCouponName = couponName.text.toString()
            var getCouponValue = couponValue.text.toString()

            if(getStartDate!="" && getEndDate!="" && getCouponName!="" && getCouponValue!=""){
                val id = db.collection("reviews").document().id
                var newcp = coupon(id,getCouponName,getCouponValue.toDouble(),getStartDate,getEndDate,
                user?.uid.toString())
                /*var newcp = coupon(id,getCouponName,getCouponValue.toDouble()/100.0,getStartDate,getEndDate,
                    "blabla")*/
                addCoupon(newcp,id)

                var reply = Intent()
                reply.putExtra("100", newcp)
                setResult(Activity.RESULT_OK, reply)
                finish()
            }

        }

    }
}