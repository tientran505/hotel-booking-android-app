package com.example.stayfinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.stayfinder.hotel.hotel_detail.ViewPagerAdapter
import java.net.URL
import java.text.DecimalFormat

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
class RoomAdapter (private var item: ArrayList<Room>) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    private var context: Context? = null
    var onButtonClick: ((Int) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val viewpager = listItemView.findViewById(R.id.viewPager) as ViewPager
        var pageAdapter: ViewPagerAdapter? = null
        val roomtypeTv = listItemView.findViewById<TextView>(R.id.roomtitleTv)
        val descriptionTv=listItemView.findViewById<TextView>(R.id.descriptionTv)
        val daterangeTv = listItemView.findViewById<TextView>(R.id.date_rangeTv)
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
        val moneyexchange = DecimalFormat("###,###,###,###.##"+"$");
        holder.roomtypeTv.setText(this.item[position].roomType)
        println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        holder.pageAdapter = this.context?.let { ViewPagerAdapter(this.item[position].img, it) }
        holder.viewpager?.adapter = holder.pageAdapter
        holder.descriptionTv.setText(this.item[position].description)
        holder.daterangeTv.setText(this.item[position].daterange)
        holder.discountpriceTv.setText(moneyexchange.format(this.item[position].discount_price*this.item[position].numberofdate))
        holder.originpriceTv.setText(moneyexchange.format(this.item[position].origin_price*this.item[position].numberofdate))
        holder.percentagediscountTv.setText(" - "+ (this.item[position].percentage_discount*100).toString()+ "%")

    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<Room>){
        this.item = list
        this.notifyDataSetChanged()
    }
}