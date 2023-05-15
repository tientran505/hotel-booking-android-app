package com.example.stayfinder.partner.booking.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.adapter.CouponInfoAdapter
import com.example.stayfinder.partner.property.adapter.coupon_adapter
import java.text.DecimalFormat

data class room_book(
    var num_of_guests:Int = 0,
    var price:Double = 0.0,
){

}

class BookingRoomAdapter(private val lists: List<room_book>):
    RecyclerView.Adapter<BookingRoomAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val roomTv = listItemView.findViewById<TextView>(R.id.room_num)
        val numTv = listItemView.findViewById<TextView>(R.id.num)
        val priceTv = listItemView.findViewById<TextView>(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            BookingRoomAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.partner_custom_booking_rooms, parent, false)
// Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: BookingRoomAdapter.ViewHolder, position: Int) {
        val num_of_guests = holder.numTv
        val price = holder.priceTv
        val room = holder.roomTv

        num_of_guests.setText(lists[position].num_of_guests.toString())
        val moneyexchange = DecimalFormat("###,###,###,###.##"+"đ");
        price.setText(moneyexchange.format(this.lists[position].price).toString())
        room.setText("Room" + position)
    }

}