package com.example.stayfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.booking.PersonalConfirmation
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
import java.util.concurrent.TimeUnit

class RoomActivity : AppCompatActivity() , CoroutineScope by MainScope() {
    var itemList = arrayListOf<Room>()
    val db = Firebase.firestore
    var numberofdate : Long = 0
    var daterange: String = ""
    lateinit var adapter : RoomAdapter
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    lateinit var progressBar: ProgressBar
    val moneyexchange = DecimalFormat("###,###,###,###.##"+" vnđ");
//    costTv.setText(moneyexchange.format(price*daysDiff))
    fun parseDate(startdate: String, enddate: String)  {
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val start = formatter.parse(startdate)
        val end = formatter.parse(enddate)
        numberofdate = TimeUnit.DAYS.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS)
        daterange = "From "+ startdate +" to "+ enddate
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        val bundle = intent.extras
        val hotel_id = bundle!!.getString("hotel_id")!!
        val dateStart = bundle!!.getString("dateStart")!!
        val dateEnd = bundle!!.getString("dateEnd")!!
        println("dateStart"+ dateStart +"dateend"+dateEnd)
        parseDate(dateStart,dateEnd)
        progressBar = findViewById(R.id.savedListPB)
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
         adapter = RoomAdapter(itemList)
        recyclerview?.adapter = adapter
        launch {
            loadRoomLists(hotel_id)
        }
        adapter.onButtonClick = { pos ->
            startActivity(Intent(this, PersonalConfirmation::class.java))
        }
    }

    private suspend fun loadRoomLists(hotel_id: String) {
        println("hotel_id"+hotel_id)
        val documents = db.collection("rooms")
            .whereEqualTo("hotel_id", hotel_id)
//            .orderBy("discount_price", Query.Direction.ASCENDING)
            .get()
            .await()
        for (document in documents) {
            val l = document.toObject(rooms::class.java)
            println("l" + l)
            itemList.add(Room(l,daterange, numberofdate))
            println("Room" + Room(l,daterange, numberofdate))
            adapter.notifyItemInserted(itemList.size -1)
//            itemList.add(SavedList(l.name_list,l.number_of_item.toString() + " items saved", l.id))
//            listadapter.notifyItemInserted(savedList.size - 1)
        }
        progressBar.visibility = View.GONE
    }
}