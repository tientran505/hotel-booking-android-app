package com.example.stayfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedListAdapter(private val lists: List<SavedList>):
    RecyclerView.Adapter<SavedListAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, listener: onItemClickListener)
        : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTV)
        val infoTv = listItemView.findViewById<TextView>(R.id.infoTV)

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

            SavedListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.custom_saved_item2, parent, false)
// Return a new holder instance
        return ViewHolder(contactView, mListener)
    }



    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list: SavedList = lists.get(position)
        val nameView = holder.nameTv
        val infoView = holder.infoTv

        nameView.setText(this.lists[position].titlename)
        infoView.setText(this.lists[position].props)
    }
}