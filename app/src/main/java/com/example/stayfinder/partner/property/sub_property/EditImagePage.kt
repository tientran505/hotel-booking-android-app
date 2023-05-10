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
    lateinit var id:String
    val storageRef = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image_page)
        nameTv = findViewById(R.id.nameTv)
        addBtn = findViewById(R.id.addBtn)
        saveBtn = findViewById(R.id.saveBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        RecyclerView = findViewById(R.id.imageRV)
        initActionBar()
        val bundle = intent.extras
        id = bundle?.getString("id")!!
        val collection = bundle?.getString("collection")!!
        val roomref = Firebase.firestore.collection(collection).document(id).get()
            .addOnSuccessListener { documents ->
                val name: String =documents.getString("name").toString()
                val listImage: ArrayList<String> = documents.get("photoUrl") as ArrayList<String>

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
                }
            }
        deleteBtn.setOnClickListener {
            ImageList.removeAll({
                it.isDelete
            })
            adapter.updateList(ImageList)
        }
        addBtn.setOnClickListener {
            activityResultLauncher.launch(appPerms)
            adapter.updateList(ImageList)
        }
        saveBtn.setOnClickListener {
            println(ImageList)
            var photoUrlUpdate: ArrayList<String> = arrayListOf()
            for (i in ImageList){
                photoUrlUpdate.add(i.imageUrl)
            }
            Firebase.firestore.collection(collection).document(id)
                .update("photoUrl",photoUrlUpdate)
                .addOnSuccessListener{
                    println("success update")
                }
                .addOnFailureListener{
                    println("failed")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    val riversRef = storageRef.child("hotels").child(id + imageUri.toString())
                    var uploadTask = riversRef.putFile(imageUri!!)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }
                        riversRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUrl = task.result.toString()
                            ImageList.add(EditImage(downloadUrl, false))
                            adapter.updateList(ImageList)
                        } else {
                        }
                    }
                }
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