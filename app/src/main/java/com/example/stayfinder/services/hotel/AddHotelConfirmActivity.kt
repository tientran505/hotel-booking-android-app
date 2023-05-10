package com.example.stayfinder.services.hotel

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import java.util.*

class AddHotelConfirmActivity : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
    lateinit var nameCollection: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel_confirm)

        nameCollection = getString(R.string.hotel_collection_name)
        db = Firebase.firestore

        initActionBar()

        val fm: FragmentManager = supportFragmentManager

        var hotel = intent.getSerializableExtra("hotelInfo") as HotelDetailModel?
        var tempUriImage = intent.getStringArrayListExtra("uriImage")
        var uuidHotel = hotel!!.id

        findViewById<TextView>(R.id.addressTv).text = hotel.address["address"]

        //save latitude longitude to hotel.map
        val latitude = intent.getDoubleExtra("latitude", 10.768622999591253)
        val longitude = intent.getDoubleExtra("longitude", 106.69537279754877)

        hotel.map = arrayListOf(latitude, longitude)


        val bundle = Bundle()
        bundle.putSerializable("BookingDetail", hotel)
        bundle.putDouble("latitude", latitude)
        bundle.putDouble("longitude", longitude)
        bundle.putStringArrayList("uriImage", tempUriImage)

        val fragAddress = SubAddressFragment()
        fragAddress.arguments = bundle;


        fm.beginTransaction().replace(R.id.frameLayout2, fragAddress).commit();


        //upload to firebase
        findViewById<Button>(R.id.confirmBtn).setOnClickListener {
            db!!.collection(nameCollection!!).document(uuidHotel!!).set(hotel)
                .addOnSuccessListener {
                    Toast.makeText(
                        this, "Hotel data added successfully", Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error adding hotel data with" + " exception: $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            if (tempUriImage != null) {
                if(tempUriImage.size > 0 ){ // Có hình mới thì xoá hết cập nhật lại
                    var hotelObj: HotelDetailModel? = null
                    db!!.collection(nameCollection!!).document(uuidHotel).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                hotelObj = document.toObject(HotelDetailModel::class.java)
                                hotelObj!!.photoUrl = ArrayList() // delete all image

                                //Update object
                                db!!.collection(nameCollection!!).document(uuidHotel).set(hotelObj!!)
                            }
                        }
                }
                for (i in 0 until tempUriImage.size) {
                    var imgUri = Uri.parse(tempUriImage[i])
                    val fileName = "$uuidHotel-$i"
                    Handler().postDelayed({
                    uploadImg(imgUri, fileName, uuidHotel!!)
                    }, 2000)
                }
            }


            val intent = Intent(this, PartnerMainActivity::class.java)
            startActivity(intent)
            Handler().postDelayed({
                finishAffinity()
            }, 10000)
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
        menu?.title = "Hotel Location"
    }

    fun getLocationFromAddress( strAddress: String?): LatLng? {
        val coder = Geocoder(this, Locale.getDefault())
        val address: List<Address>?
        var LatLan: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress!!, 1)
            if (address == null) {
                return null
            }
            val location = address[0]
            LatLan = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return LatLan
    }

    private fun uploadImg(imageUri: Uri, nameFile: String, uuid: String) {
        val storageRef = Firebase.storage.reference
        val imgRef = storageRef.child("imgsTest")
        val imageRef = imgRef.child(nameFile)
        var imgUrl: String? = null

        imageRef.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(baseContext, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

            imageRef.downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    imgUrl = it.result.toString()
                    var hotelObj: HotelDetailModel? = null
                    db!!.collection(nameCollection!!).document(uuid).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                hotelObj = document.toObject(HotelDetailModel::class.java)
                                hotelObj!!.photoUrl.add(imgUrl!!) //=> add new image.

                                //Update object
                                db!!.collection(nameCollection!!).document(uuid).set(hotelObj!!)
                            }
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(baseContext, "Error uploading image", Toast.LENGTH_SHORT).show()
            }

        }
    }

}