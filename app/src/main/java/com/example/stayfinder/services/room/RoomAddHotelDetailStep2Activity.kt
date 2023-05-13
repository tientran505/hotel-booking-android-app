package com.example.stayfinder.services.room

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
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

    private lateinit var minGuestSpinner: MaterialAutoCompleteTextView

    private lateinit var priceSummaryLL: LinearLayout

    private val numberFormat: NumberFormat = DecimalFormat("#,##0")

    private lateinit var room: RoomDetailModel

    private var isValidPrice = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail_step2)

        initActionBar()

        room = intent.getSerializableExtra("roomInfo") as RoomDetailModel
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
        priceSummaryLL = findViewById(R.id.priceSummaryLL)
        minGuestSpinner = (findViewById<TextInputLayout>(R.id.minAvailableGuest).editText as? MaterialAutoCompleteTextView)!!

//        discountET.editText?.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                handleSummary()
//            }
//        }

        handleMinGuest(room!!.guest_available as Int)

        priceInfo.text = "Price for ${room?.guest_available} guest(s)"
        findViewById<TextView>(R.id.offerInfo).text = "Do you offer a lower rate when there are fewer than ${room?.guest_available} guests?"

        if (room.guest_available == 1) {
            selectorCV.visibility = View.GONE
        }

        val nextBtn = findViewById<Button>(R.id.nextBtn)

        discountET.editText?.isEnabled = false

        spinnerRate.setOnItemClickListener { adapterView, _, i, _ ->
            val selectedItem = adapterView.getItemAtPosition(i)

            handleSummary()

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
                            handleSummary()
                            if (text.isNotEmpty()) {
                                val number = text.toIntOrNull()
                                if (number == null || number < 0 || number > 99) {
                                    discountET.error = "Rate should be 0-99%"

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
                            handleSummary()

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
            priceEt.editText?.setText( room.origin_price.toString())
        }

        calendar.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->

            val calenderTemp: Calendar = Calendar.getInstance()
            calenderTemp.set(year, month, dayOfMonth)
            calView.setDate(calenderTemp.timeInMillis, true, true)
        }

        nextBtn.setOnClickListener {
            if (!isValidPrice) {
                Toast.makeText(this, "Invalid Price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            timestamp =
                calendar.date / 1000 // bước cuối mới thêm vào object để truyền đi không bị lỗi
            val price = priceEt.editText?.text.toString().replace(".","").toDouble()
            val minGuest = if (room.guest_available == 1 || lowerRateDecline.isChecked) {1} else {
                minGuestSpinner.text.toString().split(" ")[0].toInt()
            }
            var discountType: String? = null
            if (lowerRateAccept.isChecked) {
                discountType = if (spinnerRate.text.toString() == "") {null} else {spinnerRate.text.toString()}
            }

            var perGuestDiscount: Number? = null
            if (discountType != null) {
                perGuestDiscount = if (discountET.editText?.text.toString() == "") {0.00} else {
                    discountET.editText?.text.toString().toDouble()
                }
            }

            room.apply {
                origin_price = price
                min_guest = minGuest
                discount_type = discountType
                per_guest_discount = perGuestDiscount
                origin_price = price
            }

            val intent = Intent(this, RoomAddHotelDetailStep3Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("timestamp", timestamp)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        handleSummary()
    }

    private fun handlePriceSummaryLayout(guest: Int, price: Double) {
        val inflater = LayoutInflater.from(this)
        val priceViewLayout = inflater.inflate(R.layout.room_price_summary, priceSummaryLL, false)

        val numOfGuest: TextView = priceViewLayout.findViewById(R.id.numPeople)
        val currentPrice: TextView = priceViewLayout.findViewById(R.id.currentPrice)

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        if (price > 0) {
            currentPrice.setTextColor(Color.parseColor("#01CD01"))
        }
        else {
            currentPrice.setTextColor(Color.parseColor("#D80000"))
            isValidPrice = false
        }
        currentPrice.text = numberFormat.format(price)
        numOfGuest.text = "x $guest"

        priceSummaryLL.addView(priceViewLayout)
    }


    private fun summaryWithoutDiscount() {
        if (priceSummaryLL.childCount > 0) {
            priceSummaryLL.removeAllViews()
        }

        val currentPrice = priceEt.editText?.text.toString().replace(".", "").toDouble()

        if (lowerRateDecline.isChecked) {
            handlePriceSummaryLayout(room.guest_available.toInt(), currentPrice)

        }
        else if (lowerRateAccept.isChecked) {
            val minGuest = minGuestSpinner.text.toString().split(" ")[0].toInt()
            val maxGuest = room.guest_available.toInt()

            for (i in maxGuest downTo minGuest) {
                handlePriceSummaryLayout(i, currentPrice)
            }
        }
    }

    private fun summaryWithDiscountVND() {
        clearSummaryIfNotEmpty()

        val priceET = priceEt.editText?.text.toString().replace(".", "").toDouble()
        val minGuest = minGuestSpinner.text.toString().split(" ")[0].toInt()
        val maxGuest = room.guest_available.toInt()
        val discountPrice = if (discountET.editText?.text.toString() == "") {0.00} else {
            discountET.editText?.text.toString().replace(".", "").toDouble()
        }

        for (i in maxGuest downTo minGuest) {
            val currentPrice = priceET - (maxGuest - i) * discountPrice
            handlePriceSummaryLayout(i, currentPrice)
        }
    }

    private fun summaryWithDiscountPercentage() {
        clearSummaryIfNotEmpty()

        val priceET = priceEt.editText?.text.toString().replace(".", "").toDouble()
        val minGuest = minGuestSpinner.text.toString().split(" ")[0].toInt()
        val maxGuest = room.guest_available.toInt()
        val discountPrice = if (discountET.editText?.text.toString() == "") { 0.00 } else {
            discountET.editText?.text.toString().toDouble() / 100.00
        }

        for (i in maxGuest downTo minGuest) {
            val currentPrice = priceET - (maxGuest - i) * discountPrice * priceET
            handlePriceSummaryLayout(i, currentPrice)
        }
    }

    private fun handleMinGuest(currentGuest: Int) {
        val minGuestList = arrayListOf("1 person")
        for (i in 2 until currentGuest) {
            minGuestList.add("$i people")
        }

        minGuestSpinner.setText(minGuestList.last())

        minGuestSpinner.let { spinner ->
            val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_dropdown_item_1line, minGuestList)
            spinner.setAdapter(adapter)
        }



        minGuestSpinner.setOnItemClickListener { _, _, _, _ ->
            handleSummary()
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
                handleSummary()

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

    private fun clearSummaryIfNotEmpty() {
        if (priceSummaryLL.childCount > 0) {
            priceSummaryLL.removeAllViews()
        }
    }

    private fun handleSummary() {
        val isPriceNotEmpty = priceEt.editText?.text.toString() != ""
//        val isDiscountNotEmpty = discountET.editText?.text.toString() != ""

        isValidPrice = true

        when {
            (room.guest_available.toInt() == 1 || lowerRateDecline.isChecked) && isPriceNotEmpty -> {
                summaryWithoutDiscount()
            }
            lowerRateAccept.isChecked && isPriceNotEmpty -> {
                when (spinnerRate.text.toString()) {
                    "%" -> summaryWithDiscountPercentage()
                    "VND" -> summaryWithDiscountVND()
                    else -> summaryWithoutDiscount()
                }
            }
            else -> clearSummaryIfNotEmpty()
        }
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
            handleSummary()
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