package com.example.stayfinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class HorizontalAdapter(private val bookings: List<SavedListItem>):
    RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, listener: onItemClickListener)
        : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTV)
        val infoTv = listItemView.findViewById<TextView>(R.id.infoTV)
        val img = listItemView.findViewById<ImageView>(R.id.imgTV)

        init{

            listItemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            HorizontalAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.custom_saved_item1, parent, false)
// Return a new holder instance
        return ViewHolder(contactView, mListener)
    }
    override fun getItemCount(): Int {
        return bookings.size
    }

    override fun onBindViewHolder(holder: HorizontalAdapter.ViewHolder, position: Int) {
        val booking: SavedListItem = bookings.get(position)
        val nameView = holder.nameTv
        val infoView = holder.infoTv
        val imgView = holder.img

        nameView.setText(this.bookings[position].titlename)
        infoView.setText(this.bookings[position].name)
        imgView.setImageResource(this.bookings[position].img!!)
    }
}