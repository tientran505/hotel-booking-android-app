package com.example.stayfinder.hotel.hotel_detail
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.*
import com.example.stayfinder.model.HotelDetailModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.io.Serializable
import java.net.URL

data class HotelDetails(
    var id: String = "",
    var name: String ="",
    var priceless: Double =0.0,
    var img: ArrayList<String> =ArrayList<String>(),
    var rating_overall: Double? =0.0,
    var address: address = address(),
    var description: String="",
    var noFeedback: Int =0,
    var rating: rating = rating(),
    val booking_count: Int =0,
    val facilities: ArrayList<facilities> = ArrayList<facilities>(),
):Serializable{
    constructor(a: hotels, price: Double):this(a.id,a.name,price, (a.photoUrl),a.rating_overall,a.address,a.description,a.comment_count,a.rating,a.booking_count,a.facilities)
    fun updatePriceless(priceless: Double){
        this.priceless = priceless
    }
}
fun convertStringtoURL(a: ArrayList<String>):ArrayList<URL>{
    var url = ArrayList<URL>()
    for(i in a){
        url.add(URL(i))
    }
    return url
}
class HotelDetailActivity : AppCompatActivity() , CoroutineScope by MainScope() {
    var hoteldetails:HotelDetails? = null
    var isPress = false
    lateinit var progressBar: ProgressBar
    var hotel_id: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)
        initActionBar()
        progressBar = findViewById(R.id.savedListPB)
        hotel_id=intent.getStringExtra("hotel_id")!!
        val fm: FragmentManager = supportFragmentManager

        val dateStart: Long = intent.getLongExtra("start_date", 0)
        val dateEnd: Long = intent.getLongExtra("end_date", 0)
        val bookingInformation: BookingInformation = intent.getSerializableExtra("booking_info") as BookingInformation

        val detailRoom = arrayListOf<Int>(1, 2, 0)
        val bookingBtn = findViewById<Button>(R.id.BookingBtn)

        bookingBtn.setOnClickListener {
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra("hotel_id", hotel_id);
            intent.putExtra("date_start", dateStart)
            intent.putExtra("date_end", dateEnd)
            intent.putExtra("booking_info", bookingInformation)

//            intent.putIntegerArrayListExtra("detailRoom", detailRoom)
            startActivity(intent)
        }
        val documents = Firebase.firestore.collection("hotels")
            .document(hotel_id)
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                val hotel = document.toObject(HotelDetailModel::class.java)

                val roomref = Firebase.firestore.collection("rooms").whereEqualTo("hotel_id",hotel_id).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val smallestPrice = documents.documents[0].getDouble("discount_price")
                            if (smallestPrice != null) {
                                val bundle = Bundle()
                                bundle.putLong("dateStart", dateStart)
                                bundle.putLong("dateEnd", dateEnd)

                                bundle.putIntegerArrayList("detailRoom", detailRoom)
                                bundle.putStringArrayList("photo_hotels", hotel?.photoUrl)
                                bundle.putString("hotel_name", hotel?.hotel_name)
                                bundle.putString("hotel_address", hotel?.address?.get("address"))

                                bundle.putDouble("hotel_address_lat", hotel!!.map[0])
                                bundle.putDouble("hotel_address_long", hotel!!.map[1])

                                bundle.putString("hotel_description", hotel!!.description)
                                if (hotel.rating_overall != null) {
                                    bundle.putDouble("hotel_rating", hotel.rating_overall!!)
                                }
                                else {
                                    bundle.putDouble("hotel_rating", 0.00)
                                }
                                bundle.putInt("num_of_feedback", hotel!!.comment_count)
                                bundle.putString("hotel_id", hotel!!.id)

                                val fragImage = SubHotelDetailImage()
                                fragImage.arguments = bundle;
//                                val fragPeriod = SubHotelDetailPeriod()
//                                fragPeriod.arguments = bundle;
                                val fragAddress = SubHotelDetailAddress()
                                fragAddress.arguments = bundle;
                                val fragDescription = SubHotelDetailDescription()
                                bundle.putSerializable("rating", hoteldetails?.rating)
                                fragDescription.setArguments(bundle);
                                fm.beginTransaction().replace(R.id.fame1, fragImage).commit();
//                                fm.beginTransaction().replace(R.id.fame2, fragPeriod).commit();
                                fm.beginTransaction().replace(R.id.fame3, fragAddress).commit();
                                fm.beginTransaction().replace(R.id.fame4, fragDescription).commit();
                                progressBar.visibility = View.GONE
                            }
                        }
                    }
            } else {
                Toast.makeText(this," database is Empty for result", Toast.LENGTH_SHORT).show()
                finish();
            }
        }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Cannot load Image, please try again later", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Hotel detail"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.ic_save_hotel -> {
                Toast.makeText(this, "Nhu Y code o day",
                    Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}