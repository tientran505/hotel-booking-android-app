package com.example.stayfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stayfinder.partner.property.adapter.CouponInfoAdapter
import java.text.DecimalFormat

class DetailListAdapter(private val lists: List<HotelDetail>):
    RecyclerView.Adapter<DetailListAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, listener: onItemClickListener
                           , ilistener: onIconClickListener)
        : RecyclerView.ViewHolder(listItemView) {
        val price = listItemView.findViewById<TextView>(R.id.price)
        val type = listItemView.findViewById<TextView>(R.id.type)
        val title = listItemView.findViewById<TextView>(R.id.title)
        val img = listItemView.findViewById<ImageView>(R.id.imageView2)
        val iconTv = listItemView.findViewById<ImageButton>(R.id.imageButton)

        init{
            listItemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

            iconTv.setOnClickListener{
                ilistener.onIconClick(adapterPosition)
            }
        }
    }

    private lateinit var mListener : onItemClickListener
    private lateinit var iListener : onIconClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    interface onIconClickListener {

        fun onIconClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setOnIconClickListener(listener: onIconClickListener){
        iListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            DetailListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.custom_detailed_item, parent, false)
// Return a new holder instance
        return ViewHolder(contactView, mListener, iListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: HotelDetail = lists.get(position)
        val priceView = holder.price
        val typeView = holder.type
        val titleView = holder.title
        val imgView = holder.img


        val moneyexchange = DecimalFormat("###,###,###,###.##"+"đ");

        priceView.setText(moneyexchange.format(this.lists[position].price).toString())
        typeView.setText((this.lists[position].type))
        titleView.setText(this.lists[position].title)

        //imgView.setImageResource(this.lists[position].img!!)
        Glide
            .with(holder.itemView)
            .load(this.lists[position].img!!)
            .centerCrop()
            .into(imgView);
    }

    override fun getItemCount(): Int {
        return lists.size
    }


}