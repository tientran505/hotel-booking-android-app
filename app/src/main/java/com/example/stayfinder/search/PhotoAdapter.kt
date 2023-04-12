package com.example.stayfinder.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.stayfinder.R

class PhotoAdapter(private val mContext: Context,
                   private val mListPhoto: List<Photo>?): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val imgPhoto: ImageView = listItemView.findViewById(R.id.imgPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = mListPhoto?.get(position)

        if (photo != null) {
            Glide.with(mContext)
                .load(photo.getResourceId())
                .into(holder.imgPhoto)
        }
    }

    override fun getItemCount(): Int {
        if (mListPhoto != null) {
            return mListPhoto.size
        }
        return 0
    }
}