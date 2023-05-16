package com.example.stayfinder.search

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.stayfinder.R
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3
import org.w3c.dom.Text
import java.lang.Float.min
import java.text.NumberFormat
import java.util.*
import kotlin.math.min

class HotelSearchAdapter(private val hotels: List<HotelDetailModel>,
                         private val mContext: Context,
                        private val currentGuest: Int,
                         private val db: FirebaseFirestore,
                        )
    : RecyclerView.Adapter<HotelSearchAdapter.ViewHolder>(){
    var onButtonClick: ((Int) -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val heartBtn: ImageButton = listItemView.findViewById(R.id.heartBtn)
        val rating: RatingBar = listItemView.findViewById(R.id.HotelSearchRB)
        val originalPrice: TextView = listItemView.findViewById(R.id.originalPriceSearchTV)
        val discountPrice: TextView = listItemView.findViewById(R.id.discountPriceSearchTV)
        val hotelName: TextView = listItemView.findViewById(R.id.hotelNameSearchTV)
        val feedbackCount: TextView = listItemView.findViewById(R.id.reviewSearchCountTV)
        val ratingTV: TextView = listItemView.findViewById(R.id.ratingSearchTV)
        var hotelVP: ViewPager2 = listItemView.findViewById(R.id.viewPager2)
        val indicator: CircleIndicator3 = listItemView.findViewById(R.id.circle_indicator)
        val cityName: TextView = listItemView.findViewById(R.id.locationSearchTV)
        var card: LinearLayout = listItemView.findViewById(R.id.hotelSearch)

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

        val photoAdapter = PhotoAdapter(mContext, hotel.photoUrl)
        holder.hotelVP.adapter = photoAdapter
        holder.indicator.setViewPager(holder.hotelVP)
        photoAdapter.registerAdapterDataObserver(holder.indicator.adapterDataObserver)

        holder.card.clipToOutline = true
        holder.card.outlineProvider = ViewOutlineProvider.BACKGROUND
        holder.heartBtn.setImageResource(R.drawable.add_circle_outline)
        holder.rating.rating = hotel.rating_overall.toFloat()

        holder.feedbackCount.text = if (hotel.comment_count <= 1) {"${hotel.comment_count} rating"}
        else {"${hotel.comment_count} ratings"}
        holder.ratingTV.text = hotel.rating_overall.toFloat().toString()

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        holder.originalPrice.visibility = View.GONE

        db.collection("rooms").whereEqualTo("hotel_id", hotel.id)
            .get()
            .addOnSuccessListener { documents ->
                var minPrice = Double.MAX_VALUE
                var percentage = 0.00

                documents.map { it.toObject(RoomDetailModel::class.java) }
                    .forEach { room ->
                        room.available_prices.firstOrNull { it.num_of_guest == currentGuest }?.price?.let { price ->
                            minPrice = min(price, minPrice)
                            percentage = room.percentage_discount.takeIf { room.applied_coupon_id.isNullOrEmpty() } ?: percentage
                        }
                    }

                with(holder) {
                    originalPrice.visibility = if (hotel.applied_coupon == null) View.GONE else View.VISIBLE

                    if (minPrice == Double.MAX_VALUE) {
                        originalPrice.text = "Can't find suitable price"
                        discountPrice.text = "Can't find suitable price"
                    }
                    else {
                        originalPrice.text = numberFormat.format(minPrice)
                        discountPrice.text = numberFormat.format(minPrice * (1 - percentage / 100.00))
                    }
                }
            }


        holder.cityName.text = hotel.address["city"]
        holder.hotelName.text = hotel.hotel_name
    }

}