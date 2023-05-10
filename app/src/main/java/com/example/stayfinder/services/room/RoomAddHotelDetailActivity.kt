package com.example.stayfinder.services.room

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup
import com.nex3z.togglebuttongroup.button.LabelToggle
import java.util.*


class RoomAddHotelDetailActivity : AppCompatActivity() {
    val arr_typeroom = arrayListOf<String>("Bed Room", "Bath Room", "Kitchen Room")
    var typesRoom: ArrayList<String> = ArrayList<String>()
    lateinit var guestStayHQ: HorizontalQuantitizer
    lateinit var bathroomHQ: HorizontalQuantitizer
    lateinit var bedroomHQ: HorizontalQuantitizer
    lateinit var areaSquareET: EditText
    lateinit var nextBtn: Button
    lateinit var categoryRoomSpinner: Spinner

    var editMode = false
    private var db: FirebaseFirestore? = null
    var nameCollection: String = "TestRoom"

    var tempRoom:RoomDetailModel = RoomDetailModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail)

        db = Firebase.firestore

        categoryRoomSpinner = findViewById(R.id.spinnerTypeRoom)
        guestStayHQ = findViewById(R.id.guestStay_HQ)
        bathroomHQ = findViewById(R.id.bathroom_HQ)
        bedroomHQ = findViewById(R.id.bedroom_HQ)
        areaSquareET = findViewById(R.id.areaET)
        nextBtn = findViewById(R.id.nextBtn)
        var descriptionEt = findViewById<EditText>(R.id.descriptionEt)
        val multi = findViewById<View>(R.id.roomSelected_MultiGroup) as MultiSelectToggleGroup

        var uuidRoom: String? = intent.extras?.getString("uuidRoom")
        var uuidHotel: String? = intent.extras?.getString("uuidHotel")

        //uuidHotel = "ddddddddd"
        //uuidRoom = "6a02338a-f780-484b-891e-b0e3ccdb116e"

        if (uuidHotel != null && uuidRoom == null) {
            uuidRoom = UUID.randomUUID().toString()
            editMode = false
        } else if (uuidHotel != null && uuidRoom != null) { // entry exist - > fill form
            editMode = true

            val docRef = db!!.collection(nameCollection!!).document(uuidRoom)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    // fill out the form
                    val room = document.toObject(RoomDetailModel::class.java)!!

                    // set spinner style room
                    var typeRoom = applicationContext.resources.getStringArray(R.array.spinner_type_room)
                    val indexTypeRoom = typeRoom.indexOf(room.room_type)
                    categoryRoomSpinner.setSelection(indexTypeRoom)

                    //set has room
                    val hasRoom = room.have_room
                    for(i in 0 until multi.childCount){
                        var toggle = multi.getChildAt(i) as LabelToggle
                        if(hasRoom.contains(toggle.text)){
                            toggle.isChecked = true
                        }
                    }

                    guestStayHQ.value = room.num_guest!!
                    bathroomHQ.value = room.num_bathroom!!
                    bedroomHQ.value = room.num_bedroom!!
                    areaSquareET.setText(room.areaSquare!!.toString())
                    descriptionEt.setText(room.description.toString())

                    tempRoom = room

                } else {
                    Toast.makeText(this, "No document to show", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { ex ->
                Toast.makeText(this, "Fail to get an entry. Ex: $ex", Toast.LENGTH_SHORT).show()
            }


        } else {
            editMode = false
            Toast.makeText(this, "Error loading room", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PartnerMainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        //var uuidRoom:String = UUID.randomUUID().toString()
//        var hotel = intent.getSerializableExtra("hotelInfo") as HotelDetailModel?
//        var tempUriImage = intent.getStringArrayListExtra("uriImage")
        //Toast.makeText(this, txt.toString(),Toast.LENGTH_SHORT).show()


        multi.setOnCheckedChangeListener { group, checkedId, isChecked ->
            typesRoom = ArrayList<String>()
            for (checkedId in group.checkedIds) {
                var toggle = multi.findViewById<LabelToggle>(checkedId)
                typesRoom.add(toggle.text as String)
            }
        }

        nextBtn.setOnClickListener {
            var room: RoomDetailModel
            var timestamp : Long? = null


            room = RoomDetailModel(
                id = uuidRoom,
                hotelId = uuidHotel!!,
                description = descriptionEt.text.toString(),
                photoUrl = ArrayList<String>(),
                //available_start_date = Timestamp.now(), //Please do not send timestamp in intent.
                origin_price = 100000.0,
                discount_price = 0.0,
                percentage_discount = 0.0,
                applied_coupon_id = null,

                room_type = categoryRoomSpinner.selectedItem.toString(),
                have_room = typesRoom,
                num_guest = guestStayHQ.value,
                num_bedroom = bedroomHQ.value,
                num_bathroom = bathroomHQ.value,
                areaSquare = if (areaSquareET.text.toString()
                        .isNotEmpty()
                ) areaSquareET.text.toString().toDouble() else null as Double?
            )

            if(editMode != false){
                timestamp = tempRoom.available_start_date?.seconds // đã chia 1000
                //tempRoom.available_start_date = null

                room.photoUrl = tempRoom.photoUrl
                room.origin_price = tempRoom.origin_price

            }

            var intent = Intent(this, RoomAddHotelDetailStep2Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("editMode", editMode)
            if(editMode == true){
                intent.putExtra("timestamp", timestamp)
            }

            startActivity(intent)

        }


    }
}