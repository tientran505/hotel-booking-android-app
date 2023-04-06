package com.example.stayfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailListAdapter(private val lists: List<HotelDetail>):
    RecyclerView.Adapter<DetailListAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, listener: onItemClickListener)
        : RecyclerView.ViewHolder(listItemView) {
        val price = listItemView.findViewById<TextView>(R.id.price)
        val type = listItemView.findViewById<TextView>(R.id.type)
        val title = listItemView.findViewById<TextView>(R.id.title)
        val img = listItemView.findViewById<ImageView>(R.id.imageView2)
        val place = listItemView.findViewById<TextView>(R.id.place)
        val point = listItemView.findViewById<TextView>(R.id.point)

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

            DetailListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.custom_detailed_item, parent, false)
// Return a new holder instance
        return ViewHolder(contactView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: HotelDetail = lists.get(position)
        val priceView = holder.price
        val typeView = holder.type
        val titleView = holder.title
        val imgView = holder.img
        val placeView = holder.place
        val pointView = holder.point

        priceView.setText(this.lists[position].getSalePrice().toString())
        typeView.setText((this.lists[position].type))
        titleView.setText(this.lists[position].title)
        imgView.setImageResource(this.lists[position].img!!)
        placeView.setText(this.lists[position].place)
        pointView.setText(this.lists[position].point.toString())
    }

    override fun getItemCount(): Int {
        return lists.size
    }


}