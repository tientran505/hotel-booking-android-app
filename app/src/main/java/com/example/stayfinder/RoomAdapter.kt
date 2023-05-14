package com.example.stayfinder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.stayfinder.hotel.hotel_detail.ViewPagerAdapter
import com.example.stayfinder.model.RoomDetailModel
import com.google.android.flexbox.FlexboxLayout
import android.view.ViewGroup.LayoutParams
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

data class Room(
    var id: String ="",
    var hoteli_id: String ="",
    val roomType: String ="",
    val description: String ="",
    val img: ArrayList<String>,
    var available_start_date: String ="",
    var origin_price: Double =0.0,
    var discount_price: Double =0.0,
    var percentage_discount: Double = 0.0,
    var daterange: String = "",
    var numberofdate: Long = 0,
    ):java.io.Serializable{
        constructor(r: rooms): this(r.id,r.hotel_id,r.room_type,r.description,r.photoUrl,r.available_start_date
        ,r.origin_price,r.discount_price,r.percentage_discount,"",0)
    constructor(r: rooms, daterange: String, numberofdate: Long): this(r.id,r.hotel_id,r.room_type,r.description,r.photoUrl,r.available_start_date
        ,r.origin_price,r.discount_price,r.percentage_discount,daterange,numberofdate)
    }
class RoomAdapter (private var mContext: Context,
                   private var item: ArrayList<RoomDetailModel>,
                   private val currentGuest: Int) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    private var context: Context? = null
    var onButtonClick: ((Int) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val viewpager = listItemView.findViewById(R.id.viewPager) as ViewPager
        var pageAdapter: ViewPagerAdapter? = null
        val roomName = listItemView.findViewById<TextView>(R.id.roomtitleTv)
        val bedInfoTV = listItemView.findViewById<TextView>(R.id.bedInfo)
        val descriptionTv=listItemView.findViewById<TextView>(R.id.descriptionTv)
        val facilitiesFlexBox = listItemView.findViewById<FlexboxLayout>(R.id.facilitiesFlex)
        val guestInfo = listItemView.findViewById<TextView>(R.id.guestInfo)
        val discountpriceTv = listItemView.findViewById<TextView>(R.id.discount_priceTv)
        val originpriceTv = listItemView.findViewById<TextView>(R.id.originalPriceTV)
        val percentagediscountTv = listItemView.findViewById<TextView>(R.id.percentage_discountTv)
        val bookingBtn = listItemView.findViewById<Button>(R.id.bookingBtn)

        init {
            bookingBtn.setOnClickListener {
                onButtonClick?.invoke(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.room_recyclerview_layout, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: RoomAdapter.ViewHolder, position: Int) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        holder.roomName.setText(this.item[position].name)
        holder.pageAdapter = this.context?.let { ViewPagerAdapter(this.item[position].photoUrl, it) }
        holder.viewpager.adapter = holder.pageAdapter
        holder.descriptionTv.text = this.item[position].description


        val price: Double = if (this.item[position].applied_coupon_id == null) {
            holder.percentagediscountTv.visibility = View.GONE
            holder.originpriceTv.visibility = View.GONE
            this.item[position].origin_price!!
        } else {
            this.item[position].discount_price!!
        }

        if (this.item[position].discount_type == null || this.item[position].discount_type == "") {
            holder.discountpriceTv.text = numberFormat.format(price)
        }
        else {
            if (this.item[position].discount_type == "VND") {
                val maxGuest = this.item[position].guest_available
                val i = currentGuest
                val discountPrice = this.item[position].per_guest_discount!!
                val finalPrice = price - (maxGuest - i) * discountPrice
                holder.discountpriceTv.text = numberFormat.format(finalPrice)
            }
            else if (this.item[position].discount_type == "%") {
                val maxGuest = this.item[position].guest_available
                val i = currentGuest
                val discountPrice = (this.item[position].per_guest_discount!!) / 100.00
                val finalPrice = price - (maxGuest - i) * discountPrice * price
                holder.discountpriceTv.text = numberFormat.format(finalPrice)
            }
        }

        holder.guestInfo.text = "Price for $currentGuest guest(s)"

        var bedInfo = ""
        var numOfBed = 0
        val beds = this.item[position].beds
        for (bed in beds) {
            if (bedInfo != "") bedInfo = "$bedInfo + "
            numOfBed += bed.quantity
            bedInfo += "${bed.quantity} ${bed.name}"
        }
        holder.bedInfoTV.text = "$numOfBed bed(s) ($bedInfo)"

        val facilities = this.item[position].facilities
        for (facility in facilities) {
            val textView = TextView(mContext)
            textView.text = facility
            textView.setPadding(15, 0,15,0)
            holder.facilitiesFlexBox.addView(textView)
        }

    }
    override fun getItemCount(): Int {
        return item.size
    }


}