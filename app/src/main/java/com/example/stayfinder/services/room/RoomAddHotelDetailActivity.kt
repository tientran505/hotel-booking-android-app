package com.example.stayfinder.services.room

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.services.hotel.MapAddHotelActivity
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer

import com.nex3z.togglebuttongroup.MultiSelectToggleGroup
import com.nex3z.togglebuttongroup.button.LabelToggle


class RoomAddHotelDetailActivity : AppCompatActivity() {
    val arr_typeroom = arrayListOf<String>("Bed Room","Bath Room","Kitchen Room")
    var typesRoom: ArrayList<String> = ArrayList<String>()
    lateinit var guestStayHQ: HorizontalQuantitizer
    lateinit var bathroomHQ: HorizontalQuantitizer
    lateinit var bedroomHQ: HorizontalQuantitizer
    lateinit var areaSquareET: EditText
    lateinit var nextBtn: Button
    lateinit var categoryRoomSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_detail)

        categoryRoomSpinner = findViewById(R.id.spinnerTypeRoom)
        guestStayHQ = findViewById(R.id.guestStay_HQ)
        bathroomHQ = findViewById(R.id.bathroom_HQ)
        bedroomHQ = findViewById(R.id.bedroom_HQ)
        areaSquareET = findViewById(R.id.areaET)
        nextBtn = findViewById(R.id.nextBtn)

//        var hotel = intent.getSerializableExtra("hotelInfo") as HotelDetailModel?
//        var tempUriImage = intent.getStringArrayListExtra("uriImage")
        //Toast.makeText(this, txt.toString(),Toast.LENGTH_SHORT).show()

        val multi = findViewById<View>(R.id.roomSelected_MultiGroup) as MultiSelectToggleGroup
        multi.setOnCheckedChangeListener { group, checkedId, isChecked ->
            typesRoom = ArrayList<String>()
            for(checkedId in group.checkedIds){
                var toggle = multi.findViewById<LabelToggle>(checkedId)
                typesRoom.add(toggle.text as String)
            }
        }

        nextBtn.setOnClickListener {
//            hotel?.room?.set("type_room", typesRoom)
//            hotel?.room?.set("num_guest", guestStayHQ.value)
//            hotel?.room?.set("num_bathroom", bathroomHQ.value)
//            hotel?.room?.set("num_bedroom", bedroomHQ.value)
//            hotel?.room?.set("area", areaSquareET.text.toString())


        }







    }
}