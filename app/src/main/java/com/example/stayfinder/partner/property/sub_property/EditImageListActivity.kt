package com.example.stayfinder.partner.property.sub_property

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.FeedbackAdapter
import com.example.stayfinder.R
import com.example.stayfinder.hotel.hotel_detail.MyGridAdapter
import com.example.stayfinder.hotels
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.partner.property.adapter.EditImage
import com.example.stayfinder.partner.property.adapter.EditImageAdapter
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class EditImageListActivity : AppCompatActivity() {
    private var ImageList: ArrayList<EditImage> = arrayListOf<EditImage>()
    val hotel_id ="5l5PibkyeRaZRFCVPrlB"
    lateinit var recyclerView : RecyclerView
    private val REQUEST_CODE = 1752
    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image_list)
        initActionBar()
        recyclerView= findViewById(R.id.recyclerView)
        val documents = Firebase.firestore.collection("Hotels")
            .document(hotel_id)
        documents.get().addOnSuccessListener { document ->
            if (document != null) {
                ImageList.add(
                    EditImage(
                        document.getString("id"),
                        document.getString("hotel_name"),
                        document.get("photoUrl") as ArrayList<String>                            )
                )
                val roomref = Firebase.firestore.collection("rooms").whereEqualTo("hotel_id",hotel_id).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            for (i in documents) {
                                ImageList.add(
                                    EditImage(
                                        i.getString("id"),
                                        i.getString("name"),
                                        i.get("photoUrl") as ArrayList<String>
                                    )
                                )
                            }
                        }
                        recyclerView.layoutManager = LinearLayoutManager(this)

                        val adapter  =  EditImageAdapter(ImageList)
                        recyclerView.adapter= adapter
//                        adapter.onAddClick={ position ->
//                            Log.i("ttlog", position.toString())
//                            adapter.addImages(position)
//                        }
//                        adapter.onDelteClick={ position ->
//                            Log.i("ttlog", position.toString())
//                            adapter.deleteImages(position)
//                        }

                    }
            }
        }
//        uploadImgBtn!!.setOnClickListener {
//            activityResultLauncher.launch(appPerms)
//        }
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
//                    db!!.collection(nameCollection!!).document(uuid).get()
//                        .addOnSuccessListener { document ->
//                            if (document != null) {
//                                hotelObj = document.toObject(HotelDetailModel::class.java)
//
//                                if (isEditMode) { // Mode Editing
//                                    hotelObj!!.photoUrl = java.util.ArrayList() // delete all image
//                                    isEditMode = false
//
//                                }
//                                hotelObj!!.photoUrl.add(imgUrl!!) // is mode uploading => add new image.
//
//
//                                //Update object
//                                db!!.collection(nameCollection!!).document(uuid).set(hotelObj!!)
//                            }
//                        }
                }
            }.addOnFailureListener {
                Toast.makeText(baseContext, "Error uploading image", Toast.LENGTH_SHORT).show()
            }

        }

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

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Edit Image"
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