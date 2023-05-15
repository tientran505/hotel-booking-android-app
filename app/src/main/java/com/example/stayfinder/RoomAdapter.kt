package com.example.stayfinder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import me.relex.circleindicator.CircleIndicator3
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
                   private val currentGuest: BookingInformation) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
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
        val bookingBtn = listItemView.findViewById<MaterialButton>(R.id.bookingBtn)
        val priceInfoMCV = listItemView.findViewById<LinearLayout>(R.id.priceInfoLL)
        val availableGuest = listItemView.findViewById<TextView>(R.id.availableGuests)
        val circleIndicator = listItemView.findViewById<DotsIndicator>(R.id.circle_indicator_room)

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
        holder.circleIndicator.attachTo(holder.viewpager)


        holder.descriptionTv.text = this.item[position].description

        val item = this.item[position]

        val filtered_price = item.available_prices.filter { item ->
            item.num_of_guest == currentGuest.sum_people
        }

        val price = if (filtered_price.isNotEmpty()) {filtered_price[0].price} else {null}

        if (price != null) {
            if (item.applied_coupon_id == null || item.applied_coupon_id == "") {
                holder.percentagediscountTv.visibility = View.GONE
                holder.originpriceTv.visibility = View.GONE
                holder.discountpriceTv.text = numberFormat.format(price)
            }
            else {
                holder.originpriceTv.text = numberFormat.format(price)
                holder.discountpriceTv.text = numberFormat.format(price * (1 - item.percentage_discount!! / 100.00))
            }
        }
        else {
            holder.bookingBtn.isEnabled = false
            holder.bookingBtn.strokeColor = ColorStateList.valueOf(Color.GRAY)
            holder.bookingBtn.setTextColor(ColorStateList.valueOf(Color.GRAY))

            holder.bookingBtn.text = "Exceed number of guests"
            holder.priceInfoMCV.visibility = View.GONE
        }

        when (item.min_guest) {
            1 -> {
                holder.availableGuest.text = "1 person"
            }
            item.guest_available -> {
                holder.availableGuest.text = "${item.min_guest} people"
            }
            else -> {
                holder.availableGuest.text = "${item.min_guest} - ${item.guest_available} people"
            }
        }

        holder.guestInfo.text = "Price for ${currentGuest.display()}"

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