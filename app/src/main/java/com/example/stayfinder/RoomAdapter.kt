package com.example.stayfinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import java.net.URL
import java.text.DecimalFormat

data class Room(
    var id: Int,
    var hoteli_id:Int,
    val roomType: room_type,
    val description: String,
    val img: ArrayList<URL>,
    var available_start_date: String,
    var origin_price: Double,
    var discount_price: Double,
    var percentage_discount: Double,
    var daterange: String,
    var numberofdate: Int,
    ){
}
class RoomAdapter (private var item: ArrayList<Room>) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    private var context: Context? = null
    class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val viewpager = listItemView.findViewById(R.id.viewPager) as ViewPager
        var pageAdapter: ViewPagerAdapter? = null
        val roomtypeTv = listItemView.findViewById<TextView>(R.id.roomtitleTv)
        val descriptionTv=listItemView.findViewById<TextView>(R.id.descriptionTv)
        val daterangeTv = listItemView.findViewById<TextView>(R.id.date_rangeTv)
        val discountpriceTv = listItemView.findViewById<TextView>(R.id.discount_priceTv)
        val originpriceTv = listItemView.findViewById<TextView>(R.id.originalPriceTV)
        val percentagediscountTv = listItemView.findViewById<TextView>(R.id.percentage_discountTv)
        val bookingBtn = listItemView.findViewById<Button>(R.id.bookingBtn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.room_recyclerview_layout, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: RoomAdapter.ViewHolder, position: Int) {
        val moneyexchange = DecimalFormat("###,###,###,###.##"+"$");
        holder.roomtypeTv.setText(this.item[position].roomType.toString())
        holder.pageAdapter = this.context?.let { ViewPagerAdapter(this.item[position].img, it) }
        holder.viewpager?.adapter = holder.pageAdapter
        holder.descriptionTv.setText(this.item[position].description)
        holder.daterangeTv.setText(this.item[position].daterange)
        holder.discountpriceTv.setText(moneyexchange.format(this.item[position].discount_price*this.item[position].numberofdate))
        holder.originpriceTv.setText(moneyexchange.format(this.item[position].origin_price*this.item[position].numberofdate))
        holder.percentagediscountTv.setText(" - "+ (this.item[position].percentage_discount*100).toString()+ "%")
        holder.bookingBtn.setOnClickListener(){
//            ...
        }
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<Room>){
        this.item = list
        this.notifyDataSetChanged()
    }
}