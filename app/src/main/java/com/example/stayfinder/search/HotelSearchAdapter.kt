package com.example.stayfinder.search

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.stayfinder.R
import com.example.stayfinder.hotel.Hotel
import me.relex.circleindicator.CircleIndicator3
import java.text.NumberFormat
import java.util.*

class HotelSearchAdapter(private val hotels: List<Hotel>,
                         private val mContext: Context): RecyclerView.Adapter<HotelSearchAdapter.ViewHolder>(){
    var onButtonClick: ((Int) -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val heartBtn: ImageButton = listItemView.findViewById(R.id.heartBtn)
        val rating: RatingBar = listItemView.findViewById(R.id.HotelSearchRB)
        val originalPrice: TextView = listItemView.findViewById(R.id.originalPriceSearchTV)
        val discountPrice: TextView = listItemView.findViewById(R.id.discountPriceSearchTV)
        val hotelName: TextView = listItemView.findViewById(R.id.hotelNameSearchTV)
        var hotelVP: ViewPager2 = listItemView.findViewById(R.id.viewPager2)
        val indicator: CircleIndicator3 = listItemView.findViewById(R.id.circle_indicator)
        val cityName: TextView = listItemView.findViewById(R.id.locationSearchTV)
        var card: ConstraintLayout = listItemView.findViewById(R.id.hotelSearch)

        init {
            heartBtn.setOnClickListener {
                onButtonClick?.invoke(adapterPosition)
            }

            listItemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hotel_search_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = hotels.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotel = hotels[position]

        val photoAdapter = PhotoAdapter(mContext, getListPhoto())
        holder.hotelVP.adapter = photoAdapter
        holder.indicator.setViewPager(holder.hotelVP)
        photoAdapter.registerAdapterDataObserver(holder.indicator.adapterDataObserver)

        holder.card.clipToOutline = true
        holder.card.outlineProvider = ViewOutlineProvider.BACKGROUND
        holder.heartBtn.setImageResource(if (hotel.isSaved) R.drawable.ic_heart
        else R.drawable.ic_heart_red)
        holder.rating.rating = hotel.rating

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        holder.originalPrice.text = numberFormat.format(hotel.originalPrice)
        holder.originalPrice.paintFlags = holder.originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.discountPrice.text = numberFormat.format(hotel.discountPrice)
        holder.cityName.text = hotel.cityName
        holder.hotelName.text = hotel.hotelName
    }

    private fun getListPhoto(): List<Photo> {
        return listOf(
            Photo(R.drawable.img_1),
            Photo(R.drawable.img_2),
            Photo(R.drawable.img_3),
            Photo(R.drawable.img_4),
            Photo(R.drawable.img_5),
        )
    }
}