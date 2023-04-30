package com.example.stayfinder.partner.booking.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R

data class Booking(
    val title: String,
    val propertyName: String,
    val checkInDate: String,
    val numberOfNight: Int,
    val roomInfo: String,
    val status: Boolean,
    val totalPrice: String,
)

class PartnerBookingAdapter(private val myList: ArrayList<Booking>) :
    RecyclerView.Adapter<PartnerBookingAdapter.ViewHolder>() {

    inner class ViewHolder(listItem: View): RecyclerView.ViewHolder(listItem) {
        val propertyName: TextView = listItem.findViewById(R.id.propertyNameBooking)
        val statusBooking: ImageView = listItem.findViewById(R.id.statusBookingIV)
        val titleBooking: TextView = listItem.findViewById(R.id.titleBooking)
        val checkInDate: TextView = listItem.findViewById(R.id.checkInDateBooking)
        val numberOfNight: TextView = listItem.findViewById(R.id.bookingDate)
        val roomInfo: TextView = listItem.findViewById(R.id.roomInfoBooking)
        val totalPrice: TextView = listItem.findViewById(R.id.totalPriceBooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.partner_booking_list_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookingOrder = myList[position]

        holder.propertyName.text = bookingOrder.propertyName
        holder.titleBooking.text = bookingOrder.title
        holder.checkInDate.text = bookingOrder.checkInDate
        holder.totalPrice.text = bookingOrder.totalPrice
        holder.numberOfNight.text = bookingOrder.numberOfNight.toString() + " day(s) ago"
        holder.roomInfo.text = bookingOrder.roomInfo

        if (bookingOrder.status) {
            holder.statusBooking.setImageResource(R.drawable.calendar_check_svgrepo_com)
            holder.statusBooking.setBackgroundResource(R.drawable.bg_icon_green)
        }
        else {
            holder.statusBooking.setImageResource(R.drawable.calendar_cross_svgrepo_com)
            holder.statusBooking.setBackgroundResource(R.drawable.bg_icon_red)
        }
    }

    override fun getItemCount(): Int = myList.size
}