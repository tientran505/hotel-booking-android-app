package com.example.stayfinder.booking.adapter

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.google.android.flexbox.FlexboxLayout

data class Room(
    val name: String,
    val img: String,
    val numberOfPeople: Int,
)

class RoomConfirmListAdapter(
    private val context: Activity, private val myList: List<RoomDetailModel>
) : ArrayAdapter<RoomDetailModel>(context, R.layout.room_confirm_booking, myList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.room_confirm_booking, null, true)

        val roomName: TextView = rowView.findViewById(R.id.roomName)
        val roomPeople: TextView = rowView.findViewById(R.id.roomPeople)
        val roomIndex: TextView = rowView.findViewById(R.id.index)
        val img: ImageView = rowView.findViewById(R.id.roomImg)
        val facilitiesFlexBox = rowView.findViewById<FlexboxLayout>(R.id.facilitiesFlex)
        val bedInfoTV = rowView.findViewById<TextView>(R.id.bedInfo)

        roomName.text = myList[position].name
        roomIndex.text = "${position + 1} of ${myList.size}"
        Glide.with(context)
            .load(myList[position].photoUrl[0])
            .centerCrop()
            .into(img)

        when (myList[position].min_guest) {
            1 -> {
                roomPeople.text = "1 person"
            }
            myList[position].guest_available -> {
                roomPeople.text = "${myList[position].min_guest} people"
            }
            else -> {
                roomPeople.text = "${myList[position].min_guest} - ${myList[position].guest_available} people"
            }
        }

        val facilities = myList[position].facilities
        for (facility in facilities) {
            val textView = TextView(context)
            textView.text = facility
            textView.setPadding(15, 0,15,0)
            facilitiesFlexBox.addView(textView)
        }

        var bedInfo = ""
        var numOfBed = 0
        val beds = myList[position].beds
        for (bed in beds) {
            if (bedInfo != "") bedInfo = "$bedInfo + "
            numOfBed += bed.quantity
            bedInfo += "${bed.quantity} ${bed.name}"
        }
        bedInfoTV.text = "$numOfBed bed(s) ($bedInfo)"

        return rowView
    }

    override fun setNotifyOnChange(notifyOnChange: Boolean) {
        super.setNotifyOnChange(notifyOnChange)
    }
}