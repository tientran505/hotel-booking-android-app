package com.example.stayfinder.services.hotel

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class AddHotelActivity : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
    private var submitBtn: Button? = null
    private var extras: Bundle? = null
    private var nameCollection: String? = null
    private var uploadImgBtn: Button? = null

    private val REQUEST_CODE = 1752
    private var isEditMode = false


    val appPerms = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>

    init {
        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if (allAreGranted) {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE)
    }

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
        var checkedbox: MutableList<String> = mutableListOf<String>()
        var photoUrl = ArrayList<String>()


        nameCollection = getString(R.string.hotel_collection_name)
        submitBtn = findViewById(R.id.submitBtn)
        extras = intent.extras
        uploadImgBtn = findViewById(R.id.chooseImageBtn)


        var uuidHotel: String? = extras?.getString("uuidHotel")
        //uuidHotel = "2eddd0ce-9a35-4612-a3a4-27953d7aebdf"

        if (uuidHotel == null || uuidHotel == "") {
            uuidHotel = UUID.randomUUID().toString()
        } else { //Exist entry -> fill out the form
            isEditMode = true
            val docRef = db!!.collection(nameCollection!!).document(uuidHotel)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    // fill out the form
                    val hotelObj = document.toObject(HotelDetailModel::class.java)
                    photoUrl = hotelObj!!.photoUrl
                    //Check permission
                    if (Firebase.auth.currentUser?.uid.toString() != hotelObj?.owner_id) {
                        Toast.makeText(this, "You have not permitted to access", Toast.LENGTH_SHORT)
                            .show()
                        uuidHotel = UUID.randomUUID().toString()
                    } else {
                        nameHotel.setText(hotelObj.hotel_name.toString())
                        descriptionHotel.setText(hotelObj.description.toString())
                        cityHotel.setText(hotelObj.address["city"].toString())
                        districtHotel.setText(hotelObj.address["district"].toString())
                        wardHotel.setText(hotelObj.address["ward"].toString())
                        streetHotel.setText(hotelObj.address["street"].toString())
                        numberStreetHotel.setText(hotelObj.address["number"].toString())
                    }

                }

                else {
                    Toast.makeText(this, "No document to show", Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener { ex ->
                Toast.makeText(this, "Fail to get an entry. Ex: $ex", Toast.LENGTH_SHORT).show()
            }
        }

        submitBtn!!.setOnClickListener {

            //Firstly Upload photo to firebase
            val flexboxLayout = findViewById<FlexboxLayout>(R.id.flexboxLayout)

            for (i in 0 until flexboxLayout.childCount) {
                val subView: View = flexboxLayout.getChildAt(i)
                if (subView is ImageView) { // if choosed image, the iseditmode will be turn off in the first time running
                    val imageView = subView
                    var imgUri = Uri.parse(imageView.getTag().toString())
                    val fileName = "$uuidHotel-$i"
                    uploadImg(imgUri, fileName, uuidHotel!!)
                }
            }

            //Create the object of hotelDetail
            val hotel = HotelDetailModel(
                owner_id = Firebase.auth.currentUser?.uid.toString(),
                id = uuidHotel,
                hotel_name = nameHotel.text.toString(),
                description = descriptionHotel.text.toString(),
                rating = hashMapOf(
                    "cleanliness" to 0, "comfort" to 0, "services" to 0, "location" to 0
                ),
                rating_overall = null,
                address = hashMapOf(
                    "city" to cityHotel.text.toString(),
                    "number" to numberStreetHotel.text.toString(),
                    "street" to streetHotel.text.toString(),
                    "district" to districtHotel.text.toString(),
                    "ward" to wardHotel.text.toString()
                ),
                photoUrl = photoUrl,
                booking_count = 0,
                facilities = ArrayList<Objects>(),
                comment_count = 0,
                room =  hashMapOf(
                    "type_room" to  ArrayList<String>(),
                    "num_guest" to 0,
                    "num_bathroom" to 0,
                    "num_bedroom" to 0,
                    "area" to 0
                ),
                map = ArrayList<Double>()
            )

//            db!!.collection(nameCollection!!).document(uuidHotel!!).set(hotel)
//                .addOnSuccessListener {
//                    Toast.makeText(
//                        this, "Hotel data added successfully", Toast.LENGTH_SHORT
//                    ).show()
//                }.addOnFailureListener {
//                    Toast.makeText(
//                        this, "Error adding hotel data with" + " exception: $it", Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//
//            if (findViewById<CheckBox>(R.id.freeWifiCheckbox).isChecked) { // free Wifi
//
//            }
//
//            if (findViewById<CheckBox>(R.id.swimmingPool).isChecked) { // swimming Pool
//
//            }
//
//            if (findViewById<CheckBox>(R.id.fitnessCenterCheckbox).isChecked) { //fitness Center
//
//            }

            var intent = Intent(this, RoomAddHotelDetailActivity::class.java)
            intent.putExtra("hotelInfo", hotel)
            startActivity(intent)

        }

        uploadImgBtn!!.setOnClickListener {
            activityResultLauncher.launch(appPerms)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            var flex = findViewById<FlexboxLayout>(R.id.flexboxLayout)
            flex.removeAllViews()

            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    var imgView = ImageView(baseContext)
                    imgView.visibility = View.VISIBLE
                    imgView.setImageURI(null)
                    imgView.setImageURI(imageUri)
                    imgView.setPadding(5, 5, 5, 5)
                    imgView.tag = imageUri.toString()
                    imgView.scaleType = ImageView.ScaleType.CENTER_CROP

                    //imgView.maxHeight = 100
                    //imgView.requestLayout();
                    val flexparams = FlexboxLayout.LayoutParams(300, 300)
                    imgView.layoutParams = flexparams

                    flex.addView(imgView)
                }
            }
        }
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

                                if (isEditMode) { // Mode Editing
                                    hotelObj!!.photoUrl = ArrayList() // delete all image
                                    isEditMode = false

                                }
                                hotelObj!!.photoUrl.add(imgUrl!!) // is mode uploading => add new image.


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