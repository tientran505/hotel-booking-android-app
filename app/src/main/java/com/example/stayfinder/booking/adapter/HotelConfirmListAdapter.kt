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

        Log.i("ttlog", "Item $position is initialized")

        roomName.text = myList[position].name
        roomIndex.text = "${position + 1} of ${myList.size}"
//        Glide.with(context)
//            .load(R.drawable.img_1)
//            .centerCrop()
//            .into(img)
        roomPeople.text = "${myList[position].guest_available} adult(s)"

        img.setImageResource(R.drawable.img_1)

        return rowView
    }

    override fun setNotifyOnChange(notifyOnChange: Boolean) {
        super.setNotifyOnChange(notifyOnChange)
    }
}