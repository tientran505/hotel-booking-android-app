package com.example.stayfinder.booking.adapter

import android.app.Activity
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.stayfinder.R
import java.text.NumberFormat
import java.util.*

data class HotelPriceList(
    val name: String,
    val originalPrice: Float?,
    val discountPrice: Float,
)

class PriceDetailConfirmListAdapter
    (private val context: Activity, private val mList: List<HotelPriceList>)
    : ArrayAdapter<HotelPriceList>(context, R.layout.price_detail_confirm, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.price_detail_confirm, null, true)

        val roomName: TextView = rowView.findViewById(R.id.roomNamePrice)
        val originalPrice: TextView = rowView.findViewById(R.id.originalPriceConfirm)
        val discountPrice: TextView = rowView.findViewById(R.id.discountPriceConfirm)

        roomName.text = mList[position].name

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        if (mList[position].originalPrice == null) {
            originalPrice.visibility = View.GONE
        }
        else {
            originalPrice.paintFlags = originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            originalPrice.text = numberFormat.format(mList[position].originalPrice)
        }

        discountPrice.text = numberFormat.format(mList[position].discountPrice)

        return rowView
    }
}