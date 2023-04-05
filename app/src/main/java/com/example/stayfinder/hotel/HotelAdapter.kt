package com.example.stayfinder.hotel

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import java.text.NumberFormat
import java.util.*


class Hotel(val cityName: String, val hotelName: String, val rating: Float,
            val originalPrice: Float, val discountPrice: Float, var isSaved: Boolean)

class HotelAdapter(
    private val hotels: List<Hotel>,
    ) :
    RecyclerView.Adapter<HotelAdapter.ViewHolder>() {
//    private var selectedPosition = 0 // RecyclerView.NO_POSITION
    var onImageViewClick: ((Int) -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null


    override fun getItemCount(): Int = hotels.size

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val bookmarkIV: ImageView = listItemView.findViewById(R.id.bookmarkIV)
        val rating: RatingBar = listItemView.findViewById(R.id.hotelRB)
        val originalPrice: TextView = listItemView.findViewById(R.id.originalPriceTV)
        val discountPrice: TextView = listItemView.findViewById(R.id.discountPriceTV)
        val hotelName: TextView = listItemView.findViewById(R.id.hotelNameTV)
        val hotelIV: ImageView = listItemView.findViewById(R.id.hotelIV)
        var card: ConstraintLayout = listItemView.findViewById(R.id.card)


        init {
            bookmarkIV.setOnClickListener {
                onImageViewClick?.invoke(adapterPosition)
            }

            listItemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotel = hotels[position]
        holder.bookmarkIV.setImageResource(if (hotel.isSaved) R.drawable.ic_bookmark_filled else
            R.drawable.ic_bookmark_border)


        Glide.with(holder.itemView.context)
            .load("https://media-cdn.tripadvisor.com/media/photo-s/22/25/ce/ea/kingsford-hotel-manila.jpg")
            .centerCrop()
            .into(holder.hotelIV)

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        holder.card.clipToOutline = true
        holder.card.outlineProvider = ViewOutlineProvider.BACKGROUND
        holder.hotelName.text = hotel.hotelName
        holder.rating.rating = hotel.rating
        holder.originalPrice.text = numberFormat.format(hotel.originalPrice)
        holder.originalPrice.paintFlags = holder.originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.discountPrice.text = numberFormat.format(hotel.discountPrice)

    }

    fun setBookmark(position: Int) {
        hotels[position].isSaved = !hotels[position].isSaved
        notifyItemChanged(position)
    }
}
