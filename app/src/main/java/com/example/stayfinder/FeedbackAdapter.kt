package com.example.stayfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedbackAdapter (private var item: ArrayList<FeedBack>) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {
    class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTv)
        val infoTv = listItemView.findViewById<TextView>(R.id.infoTv)
        val statusTv = listItemView.findViewById<TextView>(R.id.statusTv)
        val img = listItemView.findViewById<ImageView>(R.id.itemImgv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.feedback_layout, parent, false)
        return FeedbackAdapter.ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: FeedbackAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return item.size    }
    fun updateList( list: ArrayList<FeedBack>){
        this.item = list
        this.notifyDataSetChanged()
    }
}