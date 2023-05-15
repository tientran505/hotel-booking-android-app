package com.example.stayfinder.booking

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.stayfinder.*
import com.example.stayfinder.booking.adapter.HotelPriceList
import com.example.stayfinder.booking.adapter.PriceDetailConfirmListAdapter
import com.example.stayfinder.booking.adapter.RoomConfirmListAdapter
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.NotificationModel
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.services.notification.FcmNotificationSender
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BookingConfirmation : AppCompatActivity() {
    // hotel view
    private lateinit var hotelImg: ImageView
    private lateinit var hotelType: TextView
    private lateinit var hotelName: TextView
    private lateinit var address: TextView
    private lateinit var city: TextView

    // staying time view
    private lateinit var checkinDate: TextView
    private lateinit var checkoutDate: TextView
    private lateinit var nightDays: TextView

    // Guest detail view
    private lateinit var guestName: TextView
    private lateinit var guestPhone: TextView
    private lateinit var guestEmail: TextView
    private lateinit var numberOfPeople: TextView

    // Hotel list view confirm
    private lateinit var roomLV: ListView

    // special request
    private lateinit var notes: EditText

    // price detail view
    private lateinit var priceHotelDetailLV: ListView
    private lateinit var originalPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var totalPrice: TextView

    // bottom component
    private lateinit var originalSum: TextView
    private lateinit var discountSum: TextView
    private lateinit var bookBtn: Button

    private lateinit var room: RoomDetailModel
    private lateinit var contact_info: ContactInformation
    private var dateStart: Long = 0
    private var dateEnd: Long = 0
    private lateinit var bookingInformation: BookingInformation

    private lateinit var hotel: HotelDetailModel

    val db = Firebase.firestore
    private var progressDialog: ProgressDialog? = null

    private var total_price: Double = 0.00


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation)

        room = intent.getSerializableExtra("room") as RoomDetailModel
        contact_info = intent.getSerializableExtra("contact_info") as ContactInformation
        dateStart = intent.getLongExtra("date_start", 0)
        dateEnd = intent.getLongExtra("date_end", 0)
        bookingInformation = intent.getSerializableExtra("booking_info") as BookingInformation


        initRoomConfirmListView()
        initHotelView()
        initStayingTimeView()
        initGuestDetailView()
        initNoteView()
//        initPriceDetailView()
        initBottomComponent()
        initActionBar()

        bookBtn.setOnClickListener {
            writeDB()
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

            val uuidUser = hotel.owner_id!!
            db.collection(
                getString(R.string.collection_name_token_notification)
            ).document(uuidUser).get().addOnSuccessListener { document ->
                if (document != null) {
                    var notificationObj = document.toObject(NotificationModel::class.java)
                    val token = notificationObj?.tokenUser
                    if (token != null) {
                        val sender = FcmNotificationSender(
                            token,
                            "Booking ${hotel.hotel_name}",
                            "${bookingInformation.number_of_rooms} roooms - ${bookingInformation.sum_people} people",
                            applicationContext,
                            this
                        )
                        sender.SendNotifications()
                    }
                }
            }
        }
    }

    private fun writeDB() {
        createBooking()
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Booking confirmation"
    }


    private fun initHotelView() {
        hotelImg = findViewById(R.id.hotelImgConfirm)
        hotelType = findViewById(R.id.hotelType)
        hotelName = findViewById(R.id.hotelNameBC)
        address = findViewById(R.id.addressBC)
        city = findViewById(R.id.cityConfirm)

        db.collection("hotels").document(room.hotel_id).get().addOnSuccessListener { document ->
            hotel = document.toObject(HotelDetailModel::class.java)!!

            Glide.with(this)
                .load(hotel.photoUrl[0])
                .centerCrop()
                .into(hotelImg)

            hotelType.text = "Hotel"
            hotelName.text = hotel.hotel_name
            address.text = hotel.address["address"]
            city.text = hotel.address["city"]
        }
    }

    private fun initStayingTimeView() {
        checkinDate = findViewById(R.id.checkinDateTV)
        checkoutDate = findViewById(R.id.checkoutDateTV)
        nightDays = findViewById(R.id.nightDays)

        val sdf = SimpleDateFormat("EEE, d MMM", Locale.ENGLISH)
        checkinDate.text = sdf.format(Date(dateStart))
        checkoutDate.text = sdf.format(Date(dateEnd))

        val diffInMillies: Long = Math.abs(dateEnd - dateStart)
        val diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        nightDays.text = "$diff night(s)"

    }

    private fun initGuestDetailView() {
        guestName = findViewById(R.id.guestNameTV)
        guestPhone = findViewById(R.id.guestPhoneTV)
        guestEmail = findViewById(R.id.guestMailTV)
        numberOfPeople = findViewById(R.id.pplInformation)

        guestName.text = contact_info.name
        guestPhone.text = contact_info.phone_number
        guestEmail.text = contact_info.email

        numberOfPeople.text = bookingInformation.display()
    }

    private fun initRoomConfirmListView() {
        roomLV = findViewById(R.id.roomLV)
        val mList = arrayListOf<RoomDetailModel>()
        mList.add(room)
        val roomConfirmListAdapter = RoomConfirmListAdapter(this, mList)
        roomLV.adapter = roomConfirmListAdapter
        setListViewHeightBasedOnChildren(roomLV)

//        val doc = db.collection("rooms").document(room_id).get()
//        doc.addOnSuccessListener { result ->
//            val room = result.toObject(RoomDetailModel::class.java)
//
//            val mList = arrayListOf<RoomDetailModel>()
//            if (room != null) {
//                mList.add(room)
//                val roomConfirmListAdapter = RoomConfirmListAdapter(this, mList)
//                roomLV.adapter = roomConfirmListAdapter
//                setListViewHeightBasedOnChildren(roomLV)
//            }
//        }
    }

    private fun createBooking() {
        progressDialog?.setTitle("Please wait")
        progressDialog?.setMessage("Submitting...")
        progressDialog?.show()

        val docId = db.collection("bookings").document().id

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser


        val booking = BookingDetail(
            id = docId,
            booking_information = bookingInformation,
            date_start = Timestamp(Date(dateStart)),
            date_end = Timestamp(Date(dateEnd)),
            num_of_nights = nightDays.text.toString().split(" ")[0].toInt(),
            user_id = user?.uid ?: "",
            message = notes.text.toString(),
            personal_contact = contact_info,
            hotel = this.hotel,
            rooms = arrayListOf(this.room),
            created_date = Timestamp(Date(System.currentTimeMillis())),
            status = "Active",
            total_price = total_price
        )



        db.collection("bookings").document(docId).set(booking).addOnSuccessListener {
            Toast.makeText(this, "Add booking successfully", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable {
                val intent = Intent(this, MainActivity::class.java)
                progressDialog?.dismiss()

                startActivity(intent)
                finishAffinity()
            }, 500)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Fail to upload booking", Toast.LENGTH_SHORT).show()
                progressDialog?.dismiss()

            }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val adapter = listView.adapter
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (adapter.count - 1))
        listView.layoutParams = params
    }

//    private fun initPriceConfirmListView() {
//        val mList = arrayListOf(
//            HotelPriceList("One-Bedroom Apartment", 4500000.0.toFloat(),
//                3200000.0.toFloat()),
//            HotelPriceList("Apartment with Balcony", 4500000.0.toFloat(),
//                3200000.0.toFloat()),
//            HotelPriceList("Apartment with Balcony", 4500000.0.toFloat(),
//                3200000.0.toFloat())
//        )
//
//        priceHotelDetailLV = findViewById(R.id.priceDetailLV)
//        val priceDetailAdapter = PriceDetailConfirmListAdapter(this, mList)
//
//        priceHotelDetailLV.adapter = priceDetailAdapter
//        setListViewHeightBasedOnChildren(priceHotelDetailLV)
//    }

    private fun initNoteView() {
        notes = findViewById(R.id.noteET)
    }

//    private fun initPriceDetailView() {
//        initPriceConfirmListView()
////        originalPrice = findViewById(R.id.originalConfirmPriceTV)
////        discountPrice = findViewById(R.id.discountConfirmPriceTV)
////        totalPrice = findViewById(R.id.totalPriceConfirm)
//
//    }
//
    private fun initBottomComponent() {
        originalSum = findViewById(R.id.oPriceTV)
        discountSum = findViewById(R.id.dcPriceTV)
        bookBtn = findViewById(R.id.nextBtn)

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        val filtered_price = room.available_prices.filter { item ->
            item.num_of_guest == bookingInformation.sum_people
        }

        val price = if (filtered_price.isNotEmpty()) {filtered_price[0].price} else {null}

        if (room.applied_coupon_id == null || room.applied_coupon_id == "") {
            originalSum.visibility = View.GONE
            total_price = price!!
            discountSum.text = numberFormat.format(total_price)
        }
        else {
            originalSum.text = numberFormat.format(price)
            if (price != null) {
                total_price = price * (1 - room.percentage_discount!! / 100.00)
                discountSum.text = numberFormat.format(total_price)
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
}