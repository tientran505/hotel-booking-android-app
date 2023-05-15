package com.example.stayfinder.saved.choose_item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.stayfinder.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@kotlinx.serialization.Serializable
@IgnoreExtraProperties
data class list(
    var id: String = "",
    var name_list: String = "",
    var number_of_item: Int = 0,
    var isAdded: Boolean = false,
    var doc_id:String = "",
) {

}

class HotelCollectionAdapter(val mContext: Context, private val myList: List<list>) :
RecyclerView.Adapter<HotelCollectionAdapter.ViewHolder>(){
    var onButtonClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val title: TextView = listItemView.findViewById(R.id.hotelNameItemTV)
        val imgBtn: ImageButton = listItemView.findViewById(R.id.addItemIB)

        init {
            imgBtn.setOnClickListener {
                onButtonClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_saved_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myList[position]
        holder.title.text = item.name_list
        holder.imgBtn.setImageResource(
            if (item.isAdded) R.drawable.check_circle_svgrepo_com
                else R.drawable.plus_svgrepo_com)
    }

    override fun getItemCount(): Int = myList.size
}