package com.example.stayfinder.partner.property.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R

data class Property(
    val imgUrl: String,
    val propertyName: String,
)

class PropertyAdapter(
    private val mActivity: Activity, private val myList: List<Property>
) : ArrayAdapter<Property>(mActivity, R.layout.partner_property_list_item, myList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = mActivity.layoutInflater
        val rowView: View = inflater.inflate(R.layout.partner_property_list_item, null, true)

        val img: ImageView = rowView.findViewById(R.id.imgProperty)
        val propertyTitle: TextView = rowView.findViewById(R.id.propertyName)

        val property = myList[position]
        img.setImageResource(R.drawable.img_1)

        Glide.with(mActivity)
            .load(property.imgUrl)
            .centerCrop()
            .into(img)

        propertyTitle.text = property.propertyName

        return rowView
    }
}