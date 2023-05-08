package com.example.stayfinder.partner.property.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.SavedListAdapter
import com.example.stayfinder.coupon

class CouponInfoAdapter(private val lists: List<coupon>):
    RecyclerView.Adapter<CouponInfoAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, mlistener: CouponInfoAdapter.onItemClickListener, ilistener: CouponInfoAdapter.onIconClickListener)
        : RecyclerView.ViewHolder(listItemView) {

        }
}