package com.example.stayfinder.booking

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.*
import com.example.stayfinder.booking.model.GuestForm
import com.example.stayfinder.model.RoomDetailModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.NumberFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

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

    private var dateEnd: Long = 0
    private var dateStart: Long = 0
    private lateinit var bookingInformation: BookingInformation

    val db = Firebase.firestore


    //    private lateinit var bookingDetail: BookingDetail
    private lateinit var room: RoomDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_confirmation)

        room = intent.getSerializableExtra("room") as RoomDetailModel
        dateStart = intent.getLongExtra("date_start", 0)
        dateEnd = intent.getLongExtra("date_end", 0)
        bookingInformation = intent.getSerializableExtra("booking_info") as BookingInformation

        initActionBar()
        initComponent()
        fillFormWithUser()

        nextBtn.setOnClickListener {
            if (validateForm()) {
                val intent = Intent(this, BookingConfirmation::class.java)


                val contactInformation = ContactInformation(nameET.text.toString(),
                    emailET.text.toString(), phoneET.text.toString())

                intent.putExtra("contact_info", contactInformation)
                intent.putExtra("room", room)
                intent.putExtra("date_start", dateStart)
                intent.putExtra("date_end", dateEnd)
                intent.putExtra("booking_info", bookingInformation)

                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    private fun fillFormWithUser() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            nameET.setText(user.displayName)
            emailET.setText(user.email)
            phoneET.setText(user.phoneNumber)
        }
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Your Personal Information"
    }

    private fun validateForm(): Boolean {
        if (nameET.text.isEmpty()) {
            nameET.requestFocus()
            nameET.error = "This field can't be blank"
            return false
        }

        if (emailET.text.isEmpty()) {
            emailET.requestFocus()
            emailET.error = "This field can't be blank"
            return false
        }

        if (!isEmailValid(emailET.text.toString())) {
            emailET.requestFocus()
            emailET.error = "The format email is wrong"
            return false
        }

        if (phoneET.text.isEmpty()) {
            phoneET.requestFocus()
            phoneET.error = "This field can't be blank"
            return false
        }

        return true
    }

    private fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
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
        tripTypeRB.check(workRB.id)
        nextBtn = findViewById(R.id.nextBtn)

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        val filtered_price = room.available_prices.filter { item ->
            item.num_of_guest == bookingInformation.sum_people
        }

        val price = if (filtered_price.isNotEmpty()) {filtered_price[0].price} else {null}

        if (room.applied_coupon_id == null || room.applied_coupon_id == "") {
            originalPriceTV.visibility = View.GONE
            discountPriceTV.text = numberFormat.format(price)
        }
        else {
            originalPriceTV.text = numberFormat.format(price)
            if (price != null) {
                discountPriceTV.text = numberFormat.format(price * (1 - room.percentage_discount!! / 100.00))
            }
        }

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