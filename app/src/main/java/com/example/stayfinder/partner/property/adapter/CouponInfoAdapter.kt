package com.example.stayfinder.partner.property.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R
import com.example.stayfinder.coupon

class CouponInfoAdapter(private val lists: List<coupon>):
    RecyclerView.Adapter<CouponInfoAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, mlistener: CouponInfoAdapter.onItemClickListener,
                           ilistener: CouponInfoAdapter.onIconClickListener) : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.promotionName)
        val discountValueTv = listItemView.findViewById<TextView>(R.id.discountValue)
        val startDateTv = listItemView.findViewById<TextView>(R.id.startDate)
        val endDateTv = listItemView.findViewById<TextView>(R.id.endDate)
        val iconTv = listItemView.findViewById<ImageButton>(R.id.imageButton)

        init{
            listItemView.setOnClickListener{
                mlistener.onItemClick(adapterPosition)
            }

            iconTv.setOnClickListener({
                ilistener.onIconClick(adapterPosition)
            })

        }
        }

    private lateinit var mListener : CouponInfoAdapter.onItemClickListener
    private lateinit var iListener : CouponInfoAdapter.onIconClickListener

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

            ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.partner_custom_coupon, parent, false)
// Return a new holder instance
        return ViewHolder(contactView, mListener, iListener)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: CouponInfoAdapter.ViewHolder, position: Int) {

        val nameView = holder.nameTv
        val discountValue = holder.discountValueTv
        val startDate = holder.startDateTv
        val endDate = holder.endDateTv

        val discount = this.lists[position].discount.toDouble() * 100

        nameView.setText(this.lists[position].title)
        discountValue.setText(discount.toString() + "%")
        startDate.setText(this.lists[position].startDate)
        endDate.setText(this.lists[position].endDate)
    }
}