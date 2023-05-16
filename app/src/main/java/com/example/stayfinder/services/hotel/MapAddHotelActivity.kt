package com.example.stayfinder.services.hotel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker


class MapAddHotelActivity : AppCompatActivity(), OnMapReadyCallback {

    private var db: FirebaseFirestore? = null
    lateinit var nameCollection: String
    var uuidHotel: String? = null
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        nameCollection = getString(R.string.hotel_collection_name)

        db = Firebase.firestore

        latitude = intent.getDoubleExtra("latitude",10.768622999591253)
        longitude = intent.getDoubleExtra("longitude", 106.69537279754877)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        val intent = PlacePicker.IntentBuilder()
            .setLatLong(
                latitude,
                longitude
            )  // Initial Latitude and Longitude the Map will load into
            .showLatLong(true)  // Show Coordinates in the Activity
            .setMapZoom(17.0f)  // Map Zoom Level. Default: 14.0
            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
            .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
            //.setMarkerDrawable(R.drawable.marker) // Change the default Marker Image
            //.setMarkerImageImageColor(R.color.colorPrimary)
            //.setFabColor(R.color.fabColor)
            //.setPrimaryTextColor(R.color.primaryTextColor) // Change text color of Shortened Address
            //.setSecondaryTextColor(R.color.secondaryTextColor) // Change text color of full Address
            //.setBottomViewColor(R.color.bottomViewColor) // Change Address View Background Color (Default: White)
            //.setMapRawResourceStyle(R.raw.map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
            .setMapType(MapType.NORMAL)
//            .setPlaceSearchBar(
//                true,
//                getString(R.string.MAP_API_KEY)
//            ) //Activate GooglePlace Search Bar. Default is false/not activated. SearchBar is a chargeable feature by Google
            .onlyCoordinates(true)  //Get only Coordinates from Place Picker
            .hideLocationButton(true)   //Hide Location Button (Default: false)
            .disableMarkerAnimation(true)   //Disable Marker Animation (Default: false)
            .build(this)
        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val addressData = data?.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
//                var hotel = intent.getSerializableExtra("hotelInfo") as HotelDetailModel?
//                var tempUriImage = intent.getStringArrayListExtra("uriImage")
//                uuidHotel = hotel!!.id


//                if (tempUriImage != null) {
//                    for (i in 0 until tempUriImage.size) {
//                        var imgUri = Uri.parse(tempUriImage[i])
//                        val fileName = "$uuidHotel-$i"
//                        uploadImg(imgUri, fileName, uuidHotel!!)
//                    }
//                }

//                if (addressData != null) {
//                    hotel?.map = arrayListOf<Double>(addressData.latitude, addressData.longitude)
//                }

                //println(hotel.toString())

//                db!!.collection(nameCollection!!).document(uuidHotel!!).set(hotel)
//                    .addOnSuccessListener {
//                        Toast.makeText(
//                            this, "Hotel data added successfully", Toast.LENGTH_SHORT
//                        ).show()
//                    }.addOnFailureListener {
//                        Toast.makeText(
//                            this,
//                            "Error adding hotel data with" + " exception: $it",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
                var hotel = intent.getSerializableExtra("hotelInfo") as HotelDetailModel?
                val tempUriImage = intent.getStringArrayListExtra("uriImage")
                var intent = Intent(this, AddHotelConfirmActivity::class.java)
                intent.putExtra("latitude", addressData!!.latitude)
                intent.putExtra("longitude", addressData.longitude)
                intent.putExtra("hotelInfo", hotel)
                intent.putStringArrayListExtra("uriImage", tempUriImage)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }


//    private fun uploadImg(imageUri: Uri, nameFile: String, uuid: String) {
//        val storageRef = Firebase.storage.reference
//        val imgRef = storageRef.child("imgsTest")
//        val imageRef = imgRef.child(nameFile)
//        var imgUrl: String? = null
//
//        imageRef.putFile(imageUri).addOnSuccessListener {
//            Toast.makeText(baseContext, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
//
//            imageRef.downloadUrl.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    imgUrl = it.result.toString()
//                    var hotelObj: HotelDetailModel? = null
//                    db!!.collection(nameCollection!!).document(uuid).get()
//                        .addOnSuccessListener { document ->
//                            if (document != null) {
//                                hotelObj = document.toObject(HotelDetailModel::class.java)
//                                hotelObj!!.photoUrl.add(imgUrl!!) //=> add new image.
//
//                                //Update object
//                                db!!.collection(nameCollection!!).document(uuid).set(hotelObj!!)
//                            }
//                        }
//                }
//            }.addOnFailureListener {
//                Toast.makeText(baseContext, "Error uploading image", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }

    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions()
//            .position(sydney)
//            .title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
