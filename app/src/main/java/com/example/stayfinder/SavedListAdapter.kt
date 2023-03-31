package com.example.stayfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedListAdapter(private val lists: List<SavedList>):
    RecyclerView.Adapter<SavedListAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View, mlistener: onItemClickListener, ilistener: onIconClickListener)
        : RecyclerView.ViewHolder(listItemView) {
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTV)
        val infoTv = listItemView.findViewById<TextView>(R.id.infoTV)
        val icon = listItemView.findViewById<ImageView>(R.id.imageView3)

        init{
            listItemView.setOnClickListener{
                mlistener.onItemClick(adapterPosition)
            }

            icon.setOnClickListener({
                ilistener.onIconClick(adapterPosition)
            })

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

            SavedListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
// Inflate the custom layout
        val contactView = inflater.inflate(R.layout.custom_saved_item2, parent, false)
// Return a new holder instance
        return ViewHolder(contactView, mListener, iListener)
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