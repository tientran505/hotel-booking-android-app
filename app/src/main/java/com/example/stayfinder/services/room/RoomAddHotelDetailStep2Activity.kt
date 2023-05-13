package com.example.stayfinder.services.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class RoomAddHotelDetailStep2Activity : AppCompatActivity() {
    private lateinit var calendar: CalendarView
    private lateinit var priceEt: TextInputLayout

    private lateinit var lowerRateRG : RadioGroup
    private lateinit var lowerRateAccept: RadioButton
    private lateinit var lowerRateDecline: RadioButton

    private lateinit var selectorCV: MaterialCardView
    private lateinit var offerRateCV: MaterialCardView

    private var textWatcher: TextWatcher? = null

    private lateinit var priceInfo: TextView

    private lateinit var discountET: TextInputLayout
    private lateinit var spinnerRate: MaterialAutoCompleteTextView

    private val numberFormat: NumberFormat = DecimalFormat("#,##0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail_step2)

        initActionBar()

        val room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?
        var editMode = intent.getBooleanExtra("editMode", false)
        var timestamp = intent.getLongExtra("timestamp", 1683536679)


        if(room == null){
            Toast.makeText(this,"Having error when create room", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PartnerMainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        initCardView()
        initRadioButtonGroup()

        discountET = findViewById(R.id.discountET)
        spinnerRate = (findViewById<TextInputLayout?>(R.id.spinnerRate).editText as? MaterialAutoCompleteTextView)!!
        calendar = findViewById(R.id.calendarView)
        priceEt = findViewById(R.id.priceET)
        priceInfo = findViewById(R.id.priceInfo)

        priceInfo.text = "Price for ${room?.guest_available} guest(s)"
        findViewById<TextView>(R.id.offerInfo).text = "Do you offer a lower rate when there are fewer than ${room?.guest_available} guests?"

        if (room?.guest_available == 1) {
            selectorCV.visibility = View.GONE
        }

        var nextBtn = findViewById<Button>(R.id.nextBtn)

        discountET.editText?.isEnabled = false

        spinnerRate.setOnItemClickListener { adapterView, _, i, _ ->
            val selectedItem = adapterView.getItemAtPosition(i)

            discountET.editText?.isEnabled = true
            discountET.editText?.setText("")
            discountET.error = null

            if (textWatcher != null) {
                discountET.editText?.removeTextChangedListener(textWatcher)
            }
            when (selectedItem) {
                "%" -> {
                    textWatcher = object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            val text = s.toString()
                            if (text.isNotEmpty()) {
                                val number = text.toIntOrNull()
                                if (number == null || number < 1 || number > 99) {
                                    discountET.error = "Rate should be 1-99%"

                                }
                                else {
                                    discountET.error = null
                                }
                            }
                        }
                    }
                }

                "VND" -> {
                    textWatcher = object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            val text = s.toString()
                            if (text.isNotEmpty()) {
                                val cleanString = text.replace("[^\\d]".toRegex(), "")
                                val parsed = cleanString.toDouble()
                                val formatted = numberFormat.format(parsed)
                                discountET.editText?.removeTextChangedListener(this)
                                discountET.editText?.setText(formatted)
                                discountET.editText?.setSelection(formatted.length)
                                discountET.editText?.addTextChangedListener(this)
                            }
                        }
                    }
                }
            }
            discountET.editText?.addTextChangedListener(textWatcher)
        }
        handleCurrencyChange(priceEt)
        setupCalendarView()


        if(editMode == true){ // autofill
            calendar.date = timestamp * 1000
            priceEt.editText?.setText( room?.origin_price.toString())
        }

        calendar.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->

            val calenderTemp: Calendar = Calendar.getInstance()
            calenderTemp.set(year, month, dayOfMonth)
            calView.setDate(calenderTemp.timeInMillis, true, true)
        }

        nextBtn.setOnClickListener {
            timestamp =
                calendar.date / 1000 // bước cuối mới thêm vào object để truyền đi không bị lỗi
            var price = if (priceEt.editText?.text.toString().isNotEmpty()) priceEt.editText?.text.toString()
                .toDouble() else null
            room?.origin_price = price

            val intent = Intent(this, RoomAddHotelDetailStep3Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("timestamp", timestamp)
            startActivity(intent)
        }
    }

    private fun handleCurrencyChange(editText: TextInputLayout) {
        editText.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isNotEmpty()) {
                    val cleanString = text.replace("[^\\d]".toRegex(), "")
                    val parsed = cleanString.toDouble()
                    val formatted = numberFormat.format(parsed)
                    editText.editText?.removeTextChangedListener(this)
                    editText.editText?.setText(formatted)
                    editText.editText?.setSelection(formatted.length)
                    editText.editText?.addTextChangedListener(this)
                }
            }
        })
    }

    private fun setupCalendarView() {
        val cld = Calendar.getInstance()
        val currentDate = cld.timeInMillis
        calendar.minDate = currentDate
    }

    private fun initCardView() {
        selectorCV = findViewById(R.id.selectorCV)
        offerRateCV = findViewById(R.id.offerRateCV)
    }

    private fun initRadioButtonGroup() {
        offerRateCV.visibility = View.GONE
        lowerRateRG = findViewById(R.id.lower_rateRG)
        lowerRateAccept = findViewById(R.id.lower_rate_accept)
        lowerRateDecline = findViewById(R.id.lower_rate_decline)

        lowerRateRG.setOnCheckedChangeListener { _, id ->
            when (id) {
                lowerRateAccept.id -> {
                    offerRateCV.visibility = View.VISIBLE
                }

                lowerRateDecline.id -> {
                    offerRateCV.visibility = View.GONE
                }
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Availability and Pricing"
    }
}