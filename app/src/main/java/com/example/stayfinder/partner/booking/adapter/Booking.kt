package com.example.stayfinder.partner.booking.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.BookingDetail
import com.example.stayfinder.DetailListAdapter
import com.example.stayfinder.HorizontalAdapter
import com.example.stayfinder.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Booking(
    val title: String,
    val propertyName: String,
    val checkInDate: String,
    val numberOfNight: Int,
    val roomInfo: String,
    val status: Boolean,
    val totalPrice: String,
)

class PartnerBookingAdapter(private val myList: ArrayList<BookingDetail>) :
    RecyclerView.Adapter<PartnerBookingAdapter.ViewHolder>() {

    inner class ViewHolder(listItem: View, listener: onItemClickListener)
        : RecyclerView.ViewHolder(listItem) {
        val propertyName: TextView = listItem.findViewById(R.id.propertyNameBooking)
        val statusBooking: ImageView = listItem.findViewById(R.id.statusBookingIV)
        val titleBooking: TextView = listItem.findViewById(R.id.titleBooking)
        val checkInDate: TextView = listItem.findViewById(R.id.checkInDateBooking)
        val bookingDate: TextView = listItem.findViewById(R.id.bookingDate)
        val numberOfNight: TextView = listItem.findViewById(R.id.numOfNightBooking)
        val roomInfo: TextView = listItem.findViewById(R.id.roomInfoBooking)
        val totalPrice: TextView = listItem.findViewById(R.id.totalPriceBooking)

        init{
            listItem.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

        }
    }

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.partner_booking_list_item, parent, false)
        return ViewHolder(contactView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookingOrder = myList[position]

        holder.propertyName.text = bookingOrder.hotel?.hotel_name

        when (bookingOrder.status) {
            "Active" -> {
                holder.titleBooking.text = "${bookingOrder.personal_contact?.name} made a reservation"
                holder.statusBooking.setImageResource(R.drawable.calendar_check_svgrepo_com)
                holder.statusBooking.setBackgroundResource(R.drawable.bg_icon_green)
            }
            "Completed" -> {
                holder.titleBooking.text = "${bookingOrder.personal_contact?.name}'reservation has been accepted"
                holder.statusBooking.setImageResource(R.drawable.calendar_check_svgrepo_com)
                holder.statusBooking.setBackgroundResource(R.drawable.bg_icon_green)
            }
            else -> {
                holder.titleBooking.text = "${bookingOrder.personal_contact?.name}'reservation has been cancelled"
                holder.statusBooking.setImageResource(R.drawable.calendar_cross_svgrepo_com)
                holder.statusBooking.setBackgroundResource(R.drawable.bg_icon_red)
            }
        }

        val format = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH)
        holder.checkInDate.text = format.format(bookingOrder.date_start!!.toDate())

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))
        holder.totalPrice.text = numberFormat.format(bookingOrder.total_price)
        holder.numberOfNight.text = bookingOrder.num_of_nights.toString() + " day(s)"
        holder.roomInfo.text = bookingOrder.rooms[0].name
        holder.bookingDate.text = format.format(bookingOrder.created_date!!.toDate())
    }

    override fun getItemCount(): Int = myList.size
}