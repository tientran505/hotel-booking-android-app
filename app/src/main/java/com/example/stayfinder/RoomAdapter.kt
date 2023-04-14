package com.example.stayfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL

data class Room(
    val title: String,
    val description: String,
    val roomType: room_type,
    val img: ArrayList<URL>,
    var id: Int,
    var available_start_date: String,
    var origin_price: Double,
    var discount_price: Double,
    var percentage_discount: Double,
    var applied_coupon: Int
    ){
}
class RoomAdapter (private var item: ArrayList<FeedBack>) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val titleTv = listItemView.findViewById<TextView>(R.id.titleTv)
        val RatingBar = listItemView.findViewById<RatingBar>(R.id.ratingBar)
        val dateTv = listItemView.findViewById<TextView>(R.id.dateTv)
        val contentTv = listItemView.findViewById<TextView>(R.id.contentTv)
        val avarImg = listItemView.findViewById<ImageView>(R.id.avarImg)
        val usernameTv=listItemView.findViewById<TextView>(R.id.usernameTv)
        val roomtypeTv = listItemView.findViewById<TextView>(R.id.roomtypeTv)
        val daterangeTv = listItemView.findViewById<TextView>(R.id.daterangeTv)
        val nopeopleTv = listItemView.findViewById<TextView>(R.id.nopeopleTv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.room_recyclerview_layout, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: RoomAdapter.ViewHolder, position: Int) {
        holder.titleTv.setText(this.item[position].title)
        holder.RatingBar.setRating(this.item[position].rating.toFloat());
        holder.usernameTv.setText(this.item[position].username)
        holder.contentTv.setText(this.item[position].content)
        holder.dateTv.setText(this.item[position].reviewDate)
        Glide.with(holder.itemView)
            .load(this.item[position].avarta)
            .apply(RequestOptions().centerCrop())
            .into(holder.avarImg)
        holder.roomtypeTv.setText(this.item[position].room_type)
        holder.daterangeTv.setText(this.item[position].period.toString() + " day(s) - from " +this.item[position].checkin )
        holder.nopeopleTv.setText(this.item[position].nopeople)
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<FeedBack>){
        this.item = list
        this.notifyDataSetChanged()
    }
}