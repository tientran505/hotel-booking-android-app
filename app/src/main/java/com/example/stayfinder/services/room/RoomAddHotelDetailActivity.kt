package com.example.stayfinder.services.room

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.Bed
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer
import java.util.*
import kotlin.collections.ArrayList


class RoomAddHotelDetailActivity : AppCompatActivity() {
//    val arr_typeroom = arrayListOf<String>("Bed Room", "Bath Room", "Kitchen Room")
    var typesRoom: ArrayList<String> = ArrayList<String>()
    private lateinit var guestStayHQ: HorizontalQuantitizer
//    private lateinit var bathroomHQ: HorizontalQuantitizer
//    private lateinit var bedroomHQ: HorizontalQuantitizer

    private lateinit var roomNumber: HorizontalQuantitizer

    private lateinit var areaSquareET: TextInputLayout
    private lateinit var nextBtn: Button

    private lateinit var roomName: TextInputLayout

    private lateinit var description: TextInputLayout

    private lateinit var categoryRoomSpinner: MaterialAutoCompleteTextView

    private lateinit var bedSelectionCV: MaterialCardView

    private lateinit var bedType: MaterialAutoCompleteTextView
    private lateinit var numBedType: MaterialAutoCompleteTextView

    private lateinit var bedType_row1: MaterialAutoCompleteTextView
    private lateinit var numBedType_row1: MaterialAutoCompleteTextView

    private lateinit var bedType_row2: MaterialAutoCompleteTextView
    private lateinit var numBedType_row2: MaterialAutoCompleteTextView

    private lateinit var btnDeleteRow1 : ImageButton

    private lateinit var btnDeleteRow2 : ImageButton
    private lateinit var addRow: Button

    private lateinit var row1: LinearLayout
    private lateinit var row2: LinearLayout

    var editMode = false
    private val db = Firebase.firestore
    var nameCollection: String = "TestRoom"

    var tempRoom:RoomDetailModel = RoomDetailModel()

    private lateinit var uuidHotel: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail)

        categoryRoomSpinner = (findViewById<TextInputLayout?>(R.id.spinnerTypeRoom).editText as? MaterialAutoCompleteTextView)!!

        roomNumber = findViewById(R.id.roomNumber)
        roomNumber.minValue = 1

        guestStayHQ = findViewById(R.id.guestStay_HQ)
        guestStayHQ.minValue = 1
        guestStayHQ.maxValue = 30

        bedSelectionCV = findViewById(R.id.bedSelection)
        roomName = findViewById(R.id.roomNameET)

        description = findViewById(R.id.descriptionRoomET)

        if (categoryRoomSpinner.text.isEmpty() || categoryRoomSpinner.text.toString() == "Single") {
            bedSelectionCV.visibility = View.GONE
        }

        initComponent()

//        bedroomHQ = findViewById(R.id.bedroom_HQ)
        areaSquareET = findViewById(R.id.areaET)
        nextBtn = findViewById(R.id.nextBtn)
//        val multi = findViewById<View>(R.id.roomSelected_MultiGroup) as MultiSelectToggleGroup

        var uuidRoom: String? = intent.extras?.getString("uuidRoom")
        uuidHotel = intent.extras?.getString("uuidHotel").toString()
        val items = application.resources.getStringArray(R.array.spinner_type_room)
//        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")

        //uuidHotel = "ddddddddd"
        //uuidRoom = "6a02338a-f780-484b-891e-b0e3ccdb116e"

        categoryRoomSpinner.setOnItemClickListener { adapterView, view, i, l ->
            val selectedItem = adapterView.getItemAtPosition(i)

            if (selectedItem == "Single") {
                bedSelectionCV.visibility = View.GONE

                findViewById<TextInputLayout?>(R.id.spinnerTypeBed).error = null
                findViewById<TextInputLayout?>(R.id.spinnerNumber).error = null

            }
            else {
                bedSelectionCV.visibility = View.VISIBLE
            }
        }

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
                    val typeRoom = applicationContext.resources.getStringArray(R.array.spinner_type_room)
                    categoryRoomSpinner.setText(room.room_type)
                    categoryRoomSpinner.setSimpleItems(typeRoom)
//                    categoryRoomSpinner.setSelection(indexTypeRoom)

                    //set has room
//                    val hasRoom = room.have_room
//                    for(i in 0 until multi.childCount){
//                        var toggle = multi.getChildAt(i) as LabelToggle
//                        if(hasRoom.contains(toggle.text)){
//                            toggle.isChecked = true
//                        }
//                    }

//                    guestStayHQ.value = room.num_guest!!
//                    bathroomHQ.value = room.num_bathroom!!
//                    bedroomHQ.value = room.num_bedroom!!
//                    areaSquareET.setText(room.areaSquare!!.toString())
//                    descriptionEt.setText(room.description.toString())

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


//        multi.setOnCheckedChangeListener { group, checkedId, isChecked ->
//            typesRoom = ArrayList<String>()
//            for (checkedId in group.checkedIds) {
//                var toggle = multi.findViewById<LabelToggle>(checkedId)
//                typesRoom.add(toggle.text as String)
//            }
//        }

        nextBtn.setOnClickListener {
            if (!formValidate()) {
                return@setOnClickListener
            }


            val room = getRoomData()
            Log.d("current_room", room.toString())
            val timestamp : Long? = null

//            if(editMode != false){
//                timestamp = tempRoom.available_start_date?.seconds // đã chia 1000
//                //tempRoom.available_start_date = null
//
//                room.photoUrl = tempRoom.photoUrl
//                room.origin_price = tempRoom.origin_price
//
//            }

            val intent = Intent(this, RoomAddHotelDetailStep2Activity::class.java)
            intent.putExtra("roomInfo", room)
            intent.putExtra("editMode", editMode)
            if(editMode){
                intent.putExtra("timestamp", timestamp)
            }

            startActivity(intent)

        }
    }

    private fun getRoomData(): RoomDetailModel {
        val idRoom = db.collection("rooms").document()

        val beds = ArrayList<Bed>()
        if (categoryRoomSpinner.text.toString() == "Single") {
            beds.add(Bed(name = "Single Bed", 1))
        } else {
            beds.add(
                Bed(
                    name = bedType.text.toString(),
                    quantity = numBedType.text.toString().toInt()
                )
            )

            if (row1.visibility == View.VISIBLE) {
                beds.add(
                    Bed(
                        name = bedType_row1.text.toString(),
                        quantity = numBedType_row1.text.toString().toInt()
                    )
                )
            }

            if (row2.visibility == View.VISIBLE) {
                beds.add(
                    Bed(
                        name = bedType_row2.text.toString(),
                        quantity = numBedType_row2.text.toString().toInt()
                    )
                )
            }
        }

        return RoomDetailModel(
            id = idRoom.id,
            hotel_id = uuidHotel,
            description = description.editText?.text.toString(),
            photoUrl = ArrayList(),
            //available_start_date = Timestamp.now(), //Please do not send timestamp in intent.
            origin_price = 0.0,
            discount_price = 0.0,
            name = roomName.editText?.text.toString(),
            room_available = roomNumber.value,
            room_quantity = roomNumber.value,
            percentage_discount = 0.0,
            applied_coupon_id = null,
            guest_available = if (categoryRoomSpinner.text.toString() == "Single") {
                1
            } else {
                guestStayHQ.value
            },
            beds = beds,
            room_type = categoryRoomSpinner.text.toString(),
            area_square = areaSquareET.editText?.text.toString().toDouble(),
//            created_date = FieldValue.serverTimestamp()
        )
    }

    private fun validateField(layout: TextInputLayout, value: String): Boolean {
        return if (value.isEmpty()) {
            layout.error = "This field is required"
            false
        } else {
            layout.error = null
            true
        }
    }


    private fun formValidate(): Boolean {
        val isCategoryRoomSpinnerValid = validateField(findViewById(R.id.spinnerTypeRoom), categoryRoomSpinner.text.toString())
        val isRoomNameValid = validateField(roomName, roomName.editText?.text.toString().orEmpty())
        val isAreaSquareETValid = validateField(areaSquareET, areaSquareET.editText?.text.toString().orEmpty())
        val isDescriptionValid = validateField(description, description.editText?.text.toString().orEmpty())

        var result = isCategoryRoomSpinnerValid && isRoomNameValid && isAreaSquareETValid && isDescriptionValid

        if (categoryRoomSpinner.text.toString() != "Single") {
            val isBedTypeValid = validateField(findViewById(R.id.spinnerTypeBed), bedType.text.toString())
            val isNumBedTypeValid = validateField(findViewById(R.id.spinnerNumber), numBedType.text.toString())

            result = result && isBedTypeValid && isNumBedTypeValid

            if (row1.visibility == View.VISIBLE) {
                val isBedTypeRow1Valid = validateField(findViewById(R.id.spinnerTypeBed1), bedType_row1.text.toString())
                val isNumBedTypeRow1Valid = validateField(findViewById(R.id.spinnerNumber1), numBedType_row1.text.toString())

                result = result && isBedTypeRow1Valid && isNumBedTypeRow1Valid
            }

            if (row2.visibility == View.VISIBLE) {
                val isBedTypeRow2Valid = validateField(findViewById(R.id.spinnerTypeBed2), bedType_row2.text.toString())
                val isNumBedTypeRow2Valid = validateField(findViewById(R.id.spinnerNumber2), numBedType_row2.text.toString())

                result = result && isBedTypeRow2Valid && isNumBedTypeRow2Valid
            }
        }

        return result
    }

    @SuppressLint("CutPasteId")
    private fun initComponent() {
        bedType = (findViewById<TextInputLayout?>(R.id.spinnerTypeBed).editText as? MaterialAutoCompleteTextView)!!
        bedType_row1 = (findViewById<TextInputLayout?>(R.id.spinnerTypeBed1).editText as? MaterialAutoCompleteTextView)!!
        bedType_row2 = (findViewById<TextInputLayout?>(R.id.spinnerTypeBed2).editText as? MaterialAutoCompleteTextView)!!

        numBedType = (findViewById<TextInputLayout?>(R.id.spinnerNumber).editText as? MaterialAutoCompleteTextView)!!
        numBedType_row1 = (findViewById<TextInputLayout?>(R.id.spinnerNumber1).editText as? MaterialAutoCompleteTextView)!!
        numBedType_row2 = (findViewById<TextInputLayout?>(R.id.spinnerNumber2).editText as? MaterialAutoCompleteTextView)!!

        btnDeleteRow1 = findViewById(R.id.delete_btn_row_1)
        btnDeleteRow2 = findViewById(R.id.delete_btn_row_2)
        addRow = findViewById(R.id.add_row)
        row1 = findViewById(R.id.row_1)
        row2 = findViewById(R.id.row_2)

        row1.visibility = View.GONE
        row2.visibility = View.GONE

        btnDeleteRow1.setOnClickListener {
            row1.visibility = View.GONE
            numBedType_row1.setText("")
            findViewById<TextInputLayout?>(R.id.spinnerTypeBed1).error = null
            findViewById<TextInputLayout?>(R.id.spinnerNumber1).error = null
            bedType_row1.setText("")
            addRow.visibility = View.VISIBLE
        }

        btnDeleteRow2.setOnClickListener {
            row2.visibility = View.GONE
            numBedType_row2.setText("")
            findViewById<TextInputLayout?>(R.id.spinnerTypeBed2).error = null
            findViewById<TextInputLayout?>(R.id.spinnerNumber2).error = null
            bedType_row2.setText("")
            addRow.visibility = View.VISIBLE
        }

        addRow.setOnClickListener {
            if (row1.visibility == View.GONE) {
                row1.visibility = View.VISIBLE

                if (row2.visibility == View.VISIBLE) {
                    addRow.visibility = View.GONE
                }
            }
            else if (row2.visibility == View.GONE) {
                row2.visibility = View.VISIBLE

                if (row1.visibility == View.VISIBLE) {
                    addRow.visibility = View.GONE
                }
            }
        }
    }
}