package com.example.stayfinder.services.room

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class RoomAddHotelDetailConfirmActivity : AppCompatActivity() {

    private var db: FirebaseFirestore? = null
     var nameCollection: String = "TestRoom"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail_confirm)

        db = Firebase.firestore


        var room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?
        var timestamp = intent.getStringExtra("timestamp")
        room?.available_start_date = Timestamp(timestamp!!.toLong(), 0)
        var imgs = intent.getStringArrayListExtra("img")
        var uuidHotel = room?.hotelId

        var date = SimpleDateFormat("dd/MM/yyyy")
            .format(Date((timestamp?.toLong()?.times(1000)!!)))

        findViewById<TextView>(R.id.startDateTV).text = date
        findViewById<TextView>(R.id.priceTV).text = room?.origin_price.toString()
        findViewById<TextView>(R.id.typeRoomTV).text = room?.room_type.toString()
        findViewById<TextView>(R.id.hasRoomTV).text = room?.have_room.toString()
        findViewById<TextView>(R.id.numGuestTV).text = room?.num_guest.toString()
        findViewById<TextView>(R.id.numBathroomTV).text = room?.num_bathroom.toString()
        findViewById<TextView>(R.id.numBedroomTV).text = room?.num_bedroom.toString()

        var flex = findViewById<FlexboxLayout>(R.id.flexboxLayout)
        flex.removeAllViews()

        for(i in 0 until imgs!!.size){
            var uriImg = Uri.parse(imgs[i])

            var imgView = ImageView(baseContext)
            imgView.visibility = View.VISIBLE
            imgView.setImageURI(null)
            imgView.setImageURI(uriImg)
            imgView.setPadding(5, 5, 5, 5)
            imgView.tag = uriImg.toString()
            imgView.scaleType = ImageView.ScaleType.CENTER_CROP

            //imgView.maxHeight = 100
            //imgView.requestLayout();
            val flexparams = FlexboxLayout.LayoutParams(300, 300)
            imgView.layoutParams = flexparams

            flex.addView(imgView)
        }

        var submitBtn = findViewById<Button>(R.id.submitBtn)

        submitBtn.setOnClickListener {
            //upload hotel
            if (room != null) {
                db!!.collection(nameCollection!!).document(room?.id!!).set(room)
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
            }

            //upload image
            for (i in 0 until imgs.size) {
                var imgUri = Uri.parse(imgs[i])
                val fileName = "${room?.id}-$i"
                uploadImg(imgUri, fileName, room?.id!!)
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
                    var hotelObj: RoomDetailModel? = null
                    db!!.collection(nameCollection!!).document(uuid).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                hotelObj = document.toObject(RoomDetailModel::class.java)
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