package com.example.stayfinder.partner.property.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
import java.net.URL

data class EditImage(
    var id: String? = "",
    var name: String? = "",
    var photoURL: ArrayList<String> = arrayListOf<String>(),
)
class ShowListRoom(private var item: ArrayList<String?>) : RecyclerView.Adapter<ShowListRoom.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val button = listItemView.findViewById<Button>(R.id.id)
        init {
            button.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowListRoom.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.edit_image_adapter, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ShowListRoom.ViewHolder, position: Int) {
//        println("length "+ item.size)
        println(this.item[position])
        holder.button?.setText(this.item[position])

    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<String?>){
        this.item = list
        this.notifyDataSetChanged()
    }
}

class ImageAdapter (private var item: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    var onClick: ((Int) -> Unit)? = null
    var isDelete: ArrayList<Boolean> = arrayListOf<Boolean>()
    fun deleteImages(): Int {
        return isDelete.count { it }
    }
    fun deletePosition(): ArrayList<Boolean>{
        return isDelete
    }
    @SuppressLint("ResourceAsColor")
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val logo = listItemView.findViewById<ImageView>(R.id.logo)
        init {
            for( i in item){
                isDelete.add(false)
            }
            logo.setOnClickListener {
                isDelete[adapterPosition] = !isDelete[adapterPosition]
                if(isDelete[adapterPosition]){
                    logo.setBackgroundColor(R.color.primary)
                    logo.setPadding(10)
                }
                else{
                    logo.setBackgroundColor(R.color.white)
                    logo.setPadding(0)
                }
                onClick?.invoke(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.activity_hotel_detail_2_custom_gridview, parent, false)
        return ViewHolder(contactView)
    }
    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.logo?.let {
            Glide.with(holder.itemView.context)
                .load(URL(item[position]))
                .apply(RequestOptions().centerCrop())
                .into(it)
        }
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<String>){
        this.item = list
        this.notifyDataSetChanged()
    }
}