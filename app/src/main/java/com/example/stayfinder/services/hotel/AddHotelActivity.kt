package com.example.stayfinder.services.hotel

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.CustomTextInputEditText
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.math.log


class AddHotelActivity : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
    private var submitBtn: Button? = null
    private var extras: Bundle? = null
    private var nameCollection: String? = null
    private var uploadImgBtn: Button? = null

    private lateinit var hotelName: TextInputLayout
    private lateinit var description: TextInputLayout
    private lateinit var address: TextInputLayout
    private lateinit var city: TextInputLayout

    private val REQUEST_CODE = 1752
    private var isEditMode = false

    private var selectedAddress: String? = null
    private var selectedLang: LatLng? = null

    private lateinit var placesClient: PlacesClient

    private var startAutocompleteIntentListener = View.OnClickListener { view: View ->
        view.setOnClickListener(null)
        Log.d("meocon", "hello22")

        startAutocompleteIntent()
    }


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

    // [START maps_solutions_android_autocomplete_define]
    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result: ActivityResult ->
            Log.d("meocon", "hello dong thap")
            address.editText?.setOnClickListener(startAutocompleteIntentListener)
            Log.d("meocon", "vao day")
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)

                    // Write a method to read the address components from the Place
                    // and populate the form with the address components
                    Log.d("CANCEL", "Place: " + place.addressComponents)
                    fillInAddress(place)
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.d("meocon", "User canceled autocomplete")
            }
            else {
                Log.d("meocon", "error")
            }
        } as ActivityResultCallback<ActivityResult>)
    // [END maps_solutions_android_autocomplete_define]

    // [START maps_solutions_android_autocomplete_intent]
    private fun startAutocompleteIntent() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG, Place.Field.VIEWPORT
        )

        Log.d("meocon", "hello345")

        // Build the autocomplete intent with field, country, and type filters applied
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountries(listOf("VN"))
            .setTypesFilter(listOf(TypeFilter.ADDRESS.toString().lowercase()))
            .build(this)
        startAutocomplete.launch(intent)

        Log.d("meocon", "hello345678")

    }
    // [END maps_solutions_android_autocomplete_intent]

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun fillInAddress(place: Place) {
        val components = place.addressComponents
        val address1 = StringBuilder()
        var cityName = ""


        val geocoder = Geocoder(this, Locale.getDefault())

        val addresses = geocoder.getFromLocation(place.latLng!!.latitude, place.latLng!!.longitude, 1)

        Log.d("address", addresses.toString())

        selectedAddress = addresses?.get(0)?.getAddressLine(0)
        selectedLang = place.latLng


        // Possible AddressComponent types are documented at https://goo.gle/32SJPM1
        if (components != null) {
            for (component in components.asList()) {
                Log.d("component", component.toString())
                when (component.types[0]) {
                    "street_number" -> {
                        address1.insert(0, component.name)
                    }
                    "route" -> {
                        address1.append(" ")
                        address1.append(component.shortName)
                    }
                    "administrative_area_level_1" -> {
                        if (cityName.isEmpty()) {
                            cityName = component.name
                        }
                    }
                    "locality" -> {
                        cityName = component.name

                    }
                }
            }
        }

        address.editText?.setText(address1.toString())
        city.editText?.setText(cityName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel)

        db = Firebase.firestore

        initActionBar()
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.MAP_API_KEY))
        }

        placesClient = Places.createClient(this)

        var photoUrl = ArrayList<String>()

        initComponent()

//        address.setOnClickListener(startAutocompleteIntentListener)

//        findViewById<TextInputLayout>(R.id.addressET).setOnClickListener(startAutocompleteIntentListener)

        nameCollection = getString(R.string.hotel_collection_name)
        extras = intent.extras

//        handleAddress()


        var uuidHotel: String? = extras?.getString("uuidHotel")
        //uuidHotel = "e4d58adc-171a-4509-89c0-36cb5d91e716"

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
//                        nameHotel.editText?.setText(hotelObj.hotel_name.toString())
//                        descriptionHotel.editText?.setText(hotelObj.description.toString())
//                        cityHotel.editText?.setText(hotelObj.address["city"].toString())
//                        districtHotel.setText(hotelObj.address["district"].toString())
//                        wardHotel.setText(hotelObj.address["ward"].toString())
//                        streetHotel.setText(hotelObj.address["street"].toString())
//                        numberStreetHotel.setText(hotelObj.address["number"].toString())
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
            if (!validateForm()) {
                Toast.makeText(this, "LatLng: ${selectedLang} | full address: ${selectedAddress}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Firstly Upload photo to firebase
            val flexboxLayout = findViewById<FlexboxLayout>(R.id.flexboxLayout)
            var tempUriImage: ArrayList<String> = ArrayList()

            for (i in 0 until flexboxLayout.childCount) {
                val subView: View = flexboxLayout.getChildAt(i)
                if (subView is ImageView) { // if choosed image, the iseditmode will be turn off in the first time running
                    val imageView = subView
//                    var imgUri = Uri.parse(imageView.getTag().toString())
                    tempUriImage.add(imageView.getTag().toString())
//                    val fileName = "$uuidHotel-$i"
//                    uploadImg(imgUri, fileName, uuidHotel!!)
                }
            }

            //Create the object of hotelDetail
            val hotel = HotelDetailModel(
                owner_id = Firebase.auth.currentUser?.uid.toString(),
                id = uuidHotel,
                hotel_name = hotelName.editText?.text.toString(),
                description = description.editText?.text.toString(),
                rating = hashMapOf(
                    "cleanliness" to 0, "comfort" to 0, "services" to 0, "location" to 0
                ),
                rating_overall = null,
                address = hashMapOf(
                    "address" to selectedAddress!!,
                    "city" to city.editText?.text.toString(),
                ),
                photoUrl = photoUrl,
                booking_count = 0,
                facilities = ArrayList<Objects>(),
                comment_count = 0,
                map = ArrayList<Double>()
            )
            var intent = Intent(this, AddHotelConfirmActivity::class.java)
            intent.putExtra("hotelInfo", hotel)
            intent.putExtra("latitude", selectedLang?.latitude)
            intent.putExtra("longitude", selectedLang?.longitude)

            intent.putStringArrayListExtra("uriImage", tempUriImage)
            startActivity(intent)

        }

        uploadImgBtn!!.setOnClickListener {
            activityResultLauncher.launch(appPerms)
        }
    }

    private fun validateForm() : Boolean {
        var result = true

        if (hotelName.editText?.text.toString().isEmpty()) {
            hotelName.error = "This field is required"
            result = false
        }
        else {
            hotelName.error = null
        }

        if (description.editText?.text.toString().isEmpty()) {
            description.error = "This field is required"
            result = false
        }
        else {
            description.error = null
        }

        if (address.editText?.text.toString().isEmpty()) {
            address.error = "This field is required"
            result = false
        }
        else {
            address.error = null
        }

        if (city.editText?.text.toString().isEmpty()) {
            city.error = "This field is required"
            result = false
        }
        else {
            city.error = null
        }

        return result
    }

    private fun initComponent() {
        hotelName = findViewById(R.id.nameHotelEt)
        description = findViewById(R.id.descriptionEt)
        address = findViewById(R.id.addressLayout)
        city = findViewById(R.id.cityET)
        submitBtn = findViewById(R.id.submitBtn)
        uploadImgBtn = findViewById(R.id.chooseImageBtn)

        address.editText?.setOnClickListener(startAutocompleteIntentListener)
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
        menu?.title = "Hotel Information"
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