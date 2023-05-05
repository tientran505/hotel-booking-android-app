package com.example.stayfinder.services.room

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.google.android.flexbox.FlexboxLayout

class RoomAddHotelDetailStep3Activity : AppCompatActivity() {

    private var uploadImgBtn: Button? = null
    private val REQUEST_CODE = 1752

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
        setContentView(R.layout.activity_room_add_hotel_detail_step3)

        var photoUrl = ArrayList<String>()

        uploadImgBtn = findViewById(R.id.chooseImageBtn)

        val flexboxLayout = findViewById<FlexboxLayout>(R.id.flexboxLayout)
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
}