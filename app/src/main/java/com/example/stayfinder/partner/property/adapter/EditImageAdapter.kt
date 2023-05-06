package com.example.stayfinder.partner.property.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
    var onAddClick: ((Int) -> Unit)? = null
    var onDelteClick: ((Int) -> Unit)? = null
    lateinit var dropdown: ArrayList<Boolean>
    fun addImages(position: Int) {
        println("add")
    }
    fun deleteImages(position: Int) {
        println("delete")
    }
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTv)
        val addBtn = listItemView.findViewById<Button>(R.id.addBtn)
        val deleteBtn = listItemView.findViewById<Button>(R.id.deleteBtn)
        val RecyclerView = listItemView.findViewById<RecyclerView>(R.id.imageRV)
        init {
            addBtn.setOnClickListener {
                onAddClick?.invoke(adapterPosition)
            }

            deleteBtn.setOnClickListener {
                onDelteClick?.invoke(adapterPosition)
            }
        }
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
        val adapter  =  ImageAdapter(this.item[position].photoURL )
        holder.RecyclerView?.adapter = adapter
        holder.RecyclerView.layoutManager = GridLayoutManager(holder.itemView.context,3)
        adapter.onDeleteClick={ position ->
            Log.i("ttlog", position.toString())
            adapter.deleteImages(position)
        }
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<EditImage>){
        this.item = list
        this.notifyDataSetChanged()
    }
}
class ImageAdapter (private var item: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    var onDeleteClick: ((Int) -> Unit)? = null
    fun deleteImages(position: Int) {
        println(position.toString()+" onclick")
    }
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val logo = listItemView.findViewById<ImageView>(R.id.logo)
        init {
            logo.setOnClickListener {
                onDeleteClick?.invoke(adapterPosition)
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