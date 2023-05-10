package com.example.stayfinder.partner.property.sub_property

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.partner.property.adapter.ImageAdapter
import com.example.stayfinder.partner.property.adapter.ShowListRoom
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
data class EditImage(
    var imageUrl: String,
    var isDelete: Boolean
)
class EditImagePage : AppCompatActivity() {
    private val REQUEST_CODE = 1752
    var imageUri: Uri? = null
    var ImageList: ArrayList<EditImage> = arrayListOf()
    lateinit var nameTv: TextView
    lateinit var addBtn: Button
    lateinit var deleteBtn: Button
    lateinit var saveBtn: Button
    lateinit var RecyclerView: RecyclerView
    lateinit var deletePosition: ArrayList<Boolean>
    lateinit var adapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image_page)
        nameTv = findViewById(R.id.nameTv)
        addBtn = findViewById(R.id.addBtn)
        saveBtn = findViewById(R.id.saveBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        RecyclerView = findViewById(R.id.imageRV)
        val bundle = intent.extras
        val id = bundle?.getString("id")
        val collection = bundle?.getString("collection")!!
        val roomref = Firebase.firestore.collection(collection).whereEqualTo("id", id).get()
            .addOnSuccessListener { documents ->
                var listImage: ArrayList<String> = arrayListOf()
                var name: String = ""
                if (!documents.isEmpty) {
                    for (i in documents) {
                        name = i.getString("name").toString()
                        listImage = i.get("photoUrl") as ArrayList<String>
                    }
                }
                deleteBtn.visibility= View.GONE
                nameTv.setText(name)
                for(i in listImage){
                    ImageList.add(EditImage(i,false))
                }
                adapter  =  ImageAdapter(ImageList )
                RecyclerView?.adapter = adapter
                RecyclerView.layoutManager = GridLayoutManager(this,3)
                adapter.onClick={ position ->
                    val selected = adapter.deleteImages()
                    if(selected === false) deleteBtn.visibility= View.GONE
                    else deleteBtn.visibility= View.VISIBLE
                    ImageList = adapter.deletePosition()
                    println(ImageList)
                }
            }
        deleteBtn.setOnClickListener {
            ImageList.removeAll({
                it.isDelete
            })
            println(ImageList.size)
            println(ImageList)
            adapter.updateList(ImageList)
        }
        addBtn.setOnClickListener {

        }
        saveBtn.setOnClickListener {

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
}