package com.example.stayfinder.partner.room.adapter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.adapter.Property

class RoomAdapter(
    private val mActivity: Activity, private val myList: List<ListRoomModel>
) : ArrayAdapter<ListRoomModel>(mActivity, R.layout.partner_property_list_item, myList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = mActivity.layoutInflater
        val rowView: View = inflater.inflate(R.layout.partner_property_list_item, null, true)

        val img: ImageView = rowView.findViewById(R.id.imgProperty)
        val propertyTitle: TextView = rowView.findViewById(R.id.propertyName)

        val property = myList[position]
        img.setImageResource(R.drawable.img_1)

        Glide.with(mActivity)
            .load(property.urlImage)
            .centerCrop()
            .into(img)

        propertyTitle.text = property.typeRoom

        return rowView
    }
}