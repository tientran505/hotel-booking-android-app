package com.example.stayfinder.services.room

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class RoomAddHotelDetailStep3Activity : AppCompatActivity() {

    private var uploadImgBtn: MaterialButton? = null
    private val REQUEST_CODE = 1752

    private lateinit var flexboxLayout: FlexboxLayout

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
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE)
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail_step3)

        var room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?
        var timestamp = intent.getLongExtra("timestamp", 1683536679000)

        uploadImgBtn = findViewById(R.id.chooseImageBtn)

        flexboxLayout = findViewById(R.id.flexboxLayout)
        var tempUriImage: ArrayList<String> = ArrayList()

//        for (i in 0 until flexboxLayout.childCount) {
//            val subView: View = flexboxLayout.getChildAt(i)
//            if (subView is ImageView) { // if choosed image, the iseditmode will be turn off in the first time running
//                val imageView = subView
//                tempUriImage.add(imageView.getTag().toString())
//            }
//        }

        uploadImgBtn!!.setOnClickListener {
            activityResultLauncher.launch(appPerms)
        }

        findViewById<Button>(R.id.nextBtn).setOnClickListener {
            for (i in 0 until flexboxLayout.childCount) {
                val subView: View = flexboxLayout.getChildAt(i)
                if (subView is ImageView) {
                    val imageView = subView
                    tempUriImage.add(imageView.getTag().toString())
                }
            }

//            var intent = Intent(this, RoomAddHotelDetailConfirmActivity::class.java)
            val intent = Intent(this, RoomAddHotelStep4Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("timestamp", timestamp)
            intent.putStringArrayListExtra("img", tempUriImage)
            startActivity(intent)
        }
    }

    private fun addImageToFlexboxLayout(imageUri: Uri) {
        val inflater = LayoutInflater.from(this)
        val imageViewLayout = inflater.inflate(R.layout.image_item_upload, flexboxLayout, false)

        val imageView: ImageView = imageViewLayout.findViewById(R.id.imageIV)
        val progressBar: ProgressBar = imageViewLayout.findViewById(R.id.imgPB)
        val deleteButton: Button = imageViewLayout.findViewById(R.id.removeBtn)

        imageView.setImageURI(imageUri)

        flexboxLayout.addView(imageViewLayout)

        uploadImageToFirebaseStorage(imageUri, imageView, progressBar, deleteButton)
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, imageView: ImageView, progressBar: ProgressBar, deleteButton: Button) {
        val ref = FirebaseStorage.getInstance().getReference("images/${UUID.randomUUID()}")
        val uploadTask = ref.putFile(imageUri)

        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            progressBar.progress = progress.toInt()
            deleteButton.visibility = View.GONE
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    deleteButton.visibility = View.VISIBLE
                    imageView.alpha = 1.0f
                    val downloadUri = task.result

                    Glide.with(this@RoomAddHotelDetailStep3Activity)
                        .load(downloadUri)
                        .centerCrop()
                        .into(imageView)

                    // Handle the delete button click
                    deleteButton.setOnClickListener {
                        ref.delete().addOnSuccessListener {
                            // remove the image view from the FlexboxLayout
                            flexboxLayout.removeView(imageView.parent as View)
                        }
                    }
                }
                else {
                    Toast.makeText(this@RoomAddHotelDetailStep3Activity, "Upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
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
//                    val imageUri = data.clipData!!.getItemAt(i).uri
//                    var imgView = ImageView(baseContext)
//                    imgView.visibility = View.VISIBLE
//                    imgView.setImageURI(null)
//                    imgView.setImageURI(imageUri)
//                    imgView.setPadding(5, 5, 5, 5)
//                    imgView.tag = imageUri.toString()
//                    imgView.scaleType = ImageView.ScaleType.CENTER_CROP
//
//                    //imgView.maxHeight = 100
//                    //imgView.requestLayout();
//                    val flexparams = FlexboxLayout.LayoutParams(300, 300)
//                    imgView.layoutParams = flexparams
//
//                    flex.addView(imgView)

                    val imageUri = data.clipData!!.getItemAt(i).uri
                    addImageToFlexboxLayout(imageUri)
                }
            }
            else {
                val imageUri = data?.data
                if (imageUri != null) {
                    addImageToFlexboxLayout(imageUri)
                }
            }
        }
    }
}