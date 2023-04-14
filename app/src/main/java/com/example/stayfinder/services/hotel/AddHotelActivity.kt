package com.example.stayfinder.services.hotel

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddHotelActivity : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
    private var submitBtn: Button? = null
    private var extras: Bundle? = null
    private var nameCollection:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel)

        db = Firebase.firestore

        //val uuidHotel = UUID.randomUUID().toString() // ID of hotel
        val nameHotel = findViewById<EditText>(R.id.nameHotelEt)
        val descriptionHotel = findViewById<EditText>(R.id.descriptionEt)
        val cityHotel = findViewById<EditText>(R.id.cityEt)
        val districtHotel = findViewById<EditText>(R.id.districtEt)
        val wardHotel = findViewById<EditText>(R.id.wardEt)
        val streetHotel = findViewById<EditText>(R.id.streetEt)
        val numberStreetHotel = findViewById<EditText>(R.id.numberStreetEt)
        nameCollection = getString(R.string.hotel_collection_name)
        submitBtn = findViewById(R.id.submitBtn)
        var checkedbox :MutableList<String> = mutableListOf<String>();
        extras = intent.extras
        var uuidHotel: String? = extras?.getString("uuidHotel")
        //uuidHotel = "3915b27c-90f0-41b9-b66c-8c71a4c37830"

        if(uuidHotel == null || uuidHotel == ""){
            uuidHotel = UUID.randomUUID().toString()
        }
        else{ //Exist entry -> fill out the form

            val docRef = db!!.collection(nameCollection!!).document(uuidHotel)
            docRef.get().addOnSuccessListener {
                    document ->
                if(document != null){
                    // fill out the form
                    val hotelObj = document.toObject(HotelDetailModel::class.java)

                    //Check permission
                    if(Firebase.auth.currentUser?.uid.toString() != hotelObj?.owner_id){
                        Toast.makeText(this, "You have not permitted to access", Toast.LENGTH_SHORT).show()
                        uuidHotel = UUID.randomUUID().toString()
                    }
                    else{
                        nameHotel.setText(hotelObj.hotel_name.toString())
                        descriptionHotel.setText(hotelObj.description.toString())
                        cityHotel.setText(hotelObj.address["city"].toString())
                        districtHotel.setText(hotelObj.address["district"].toString())
                        wardHotel.setText(hotelObj.address["ward"].toString())
                        streetHotel.setText(hotelObj.address["street"].toString())
                        numberStreetHotel.setText(hotelObj.address["number"].toString())
                    }

                }
                else{
                    Toast.makeText(this, "No document to show", Toast.LENGTH_SHORT).show()

                }
            }
                .addOnFailureListener {
                    ex -> Toast.makeText(this, "Fail to get an entry. Ex: $ex", Toast.LENGTH_SHORT).show()
                }
        }

        submitBtn!!.setOnClickListener {

            //Create the object of hotelDetail
            val hotel = HotelDetailModel(
                owner_id = Firebase.auth.currentUser?.uid.toString(),
                id = uuidHotel,
                hotel_name = nameHotel.text.toString(),
                description = descriptionHotel.text.toString(),
                rating = hashMapOf(
                    "cleanliness" to 0,
                    "comfort" to 0,
                    "services" to 0,
                    "location" to 0
                ),
                rating_overall= null,
                address= hashMapOf(
                    "city" to cityHotel.text.toString(),
                    "number" to numberStreetHotel.text.toString(),
                    "street" to streetHotel.text.toString(),
                    "district" to districtHotel.text.toString(),
                    "ward" to wardHotel.text.toString()
                ),
                photoUrl = ArrayList<String>(),
                booking_count= 0,
                facilities= ArrayList<Objects>(),
                comment_count=  0
            )

            db!!.collection(nameCollection!!).document(uuidHotel!!).set(hotel)
                .addOnSuccessListener{
                    Toast.makeText(
                        this, "Hotel data added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this, "Error adding hotel data with" +
                                " exception: $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            if( findViewById<CheckBox>(R.id.freeWifiCheckbox).isChecked){ // free Wifi

            }

            if(findViewById<CheckBox>(R.id.swimmingPool).isChecked){ // swimming Pool

            }

            if(findViewById<CheckBox>(R.id.fitnessCenterCheckbox).isChecked){ //fitness Center

            }
        }

    }


}