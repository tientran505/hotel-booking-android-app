package com.example.stayfinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class BookingAdapter (private var item: ArrayList<Booking>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {
    class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTv)
        val infoTv = listItemView.findViewById<TextView>(R.id.infoTv)
        val statusTv = listItemView.findViewById<TextView>(R.id.statusTv)
        val img = listItemView.findViewById<ImageView>(R.id.itemImgv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BookingAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.booking_item_layout, parent, false)
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int {
        return item.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val moneyexchange = DecimalFormat("###,###,###,###.##"+"đ");
        holder.nameTv.setText(this.item[position].titlename)
        holder.infoTv.setText(this.item[position].dateStart+ " - "+ this.item[position].dateEnd + " _ "+moneyexchange.format(this.item[position].price))
        holder.statusTv.setText(this.item[position].status)
        if(this.item[position].status == "Cancel"){
            holder.statusTv.setTextColor(Color.parseColor("#d13838"))
        }
        else if(this.item[position].status == "Active"){
            holder.statusTv.setTextColor(Color.parseColor("#3dbf3f"))
        }
        else if(this.item[position].status == "Completed") {
            holder.statusTv.setTextColor(Color.parseColor("#3d59bf"))
        }
        this.item[position].img?.let { holder.img.setImageResource(it) }
    }
    fun updateList( list: ArrayList<Booking>){
        this.item = list
        this.notifyDataSetChanged()
    }

}