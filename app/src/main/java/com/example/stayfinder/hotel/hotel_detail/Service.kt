package com.example.stayfinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL
import java.text.DecimalFormat

data class Service(val name: String, val img: String){
    constructor(f: facilities): this(f.name,f.icon)
}

class ServiceAdapter (private var item: ArrayList<Service>) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val img = listItemView.findViewById<ImageView>(R.id.img)
        val serviceTv = listItemView.findViewById<TextView>(R.id.title)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ServiceAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.service_item_layout, parent, false)
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int {
        return item.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.serviceTv.setText(this.item[position].name)
        Glide.with(holder.itemView)
            .load(URL(this.item[position].img))
            .apply(RequestOptions().centerCrop())
            .into(holder.img)
    }
    fun updateList( list: ArrayList<Service>){
        this.item = list
        this.notifyDataSetChanged()
    }

}