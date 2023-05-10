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
import com.example.stayfinder.hotels

//data class Property(
//    val imgUrl: String,
//    val propertyName: String,
//)

class PropertyAdapter(
    private val mActivity: Activity, private val myList: List<hotels>
) : ArrayAdapter<hotels>(mActivity, R.layout.partner_property_list_item, myList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = mActivity.layoutInflater
        val rowView: View = inflater.inflate(R.layout.partner_property_list_item, null, true)

        val img: ImageView = rowView.findViewById(R.id.imgProperty)
        val propertyTitle: TextView = rowView.findViewById(R.id.propertyName)

        val property = myList[position]
        img.setImageResource(R.drawable.img_1)

        var urlPhoto = ""
        urlPhoto = if (property.photoUrl.size > 0) {
            property.photoUrl[0]
        } else {
            "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80"
        }

        Glide.with(mActivity)
            .load(urlPhoto)
            .centerCrop()
            .into(img)

        propertyTitle.text = property.name

        return rowView
    }
}