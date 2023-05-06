package com.example.stayfinder.partner.property.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
import com.example.stayfinder.hotel.hotel_detail.MyGridAdapter
import java.net.URL

data class EditImage(
    var id: String? = "",
    var name: String? = "",
    var photoURL: ArrayList<String> = arrayListOf<String>(),
)
class EditImageAdapter (private var item: ArrayList<EditImage>) : RecyclerView.Adapter<EditImageAdapter.ViewHolder>() {
    class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTv)
        val addBtn = listItemView.findViewById<Button>(R.id.addBtn)
        val deleteBtn = listItemView.findViewById<Button>(R.id.deleteBtn)
        val grid = listItemView.findViewById<GridView>(R.id.gridview)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImageAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.edit_image_adapter, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: EditImageAdapter.ViewHolder, position: Int) {
        println("length "+ item.size)
        println(position)
        holder.nameTv?.setText(this.item[position].name)
        val adapter  =  MyGridAdapter(holder.itemView.context, this.item[position].photoURL )
        holder.grid?.adapter = adapter
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<EditImage>){
        this.item = list
        this.notifyDataSetChanged()
    }
}
