package com.example.stayfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.booking.PersonalConfirmation
import com.example.stayfinder.model.RoomDetailModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class RoomActivity : AppCompatActivity() , CoroutineScope by MainScope() {
    var itemList = arrayListOf<RoomDetailModel>()
    val db = Firebase.firestore
    var numberofdate : Long = 0
    var daterange: String = ""
    lateinit var adapter : RoomAdapter
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    lateinit var progressBar: ProgressBar

    fun parseDate(startdate: Long, enddate: Long)  {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val start = formatter.format(Date(startdate))
        val end = formatter.format(Date(enddate))

        val diffInMillies = kotlin.math.abs(enddate - startdate)
        numberofdate = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        daterange = "From $start to $end"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        initActionBar()
//        val bundle = intent.extras


        val hotel_id = intent.getStringExtra("hotel_id")!!
        val dateStart = intent.getLongExtra("date_start", 0)
        val dateEnd = intent.getLongExtra("date_end", 0)
        val bookingInformation: BookingInformation = intent.getSerializableExtra("booking_info") as BookingInformation

        parseDate(dateStart,dateEnd)

        progressBar = findViewById(R.id.savedListPB)
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
         adapter = RoomAdapter(this, itemList, bookingInformation)
        recyclerview?.adapter = adapter
        launch {
            loadRoomLists(hotel_id)
        }

        adapter.onButtonClick = { pos ->
            val intent = Intent(this, PersonalConfirmation::class.java)

            val currentRoom = itemList[pos]
            currentRoom.available_start_date = null
            currentRoom.created_date = null

            intent.putExtra("room", currentRoom)
            intent.putExtra("date_start", dateStart)
            intent.putExtra("date_end", dateEnd)
            intent.putExtra("booking_info", bookingInformation)

            startActivity(intent)
        }
    }

    private suspend fun loadRoomLists(hotel_id: String) {
        val documents = db.collection("rooms")
            .whereEqualTo("hotel_id", hotel_id)
//            .orderBy("discount_price", Query.Direction.ASCENDING)
            .get()
            .await()
        for (document in documents) {
            val room = document.toObject(RoomDetailModel::class.java)
            itemList.add(room)
            adapter.notifyItemInserted(itemList.size - 1)
        }
        progressBar.visibility = View.GONE
    }
    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
//        menu?.title = "Booking confirmation" // title of activity
        if(this.title =="feedback") menu?.title = "Hotel Reviews"
        if(this.title =="image") menu?.title = "Hotel Images"

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