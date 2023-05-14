package com.example.stayfinder.partner.room.adapter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class RoomAdapter(
    private val mActivity: Activity,
    private val rooms: List<RoomDetailModel>) :
RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    var onEditBtnClick: ((Int) -> Unit)? = null
    var onDeleteBtnClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val editBtn: MaterialButton = listItemView.findViewById(R.id.editBtnMB)
        val deleteBtn: MaterialButton = listItemView.findViewById(R.id.deleteBtnMB)
        val imgRoom: ShapeableImageView = listItemView.findViewById(R.id.imgRoom)
        val hotelName: TextView = listItemView.findViewById(R.id.roomNameTV)

        init {
            editBtn.setOnClickListener {
                onEditBtnClick?.invoke(adapterPosition)
            }

            deleteBtn.setOnClickListener {
                onDeleteBtnClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_custom_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = rooms[position]

        Glide.with(mActivity)
            .load(room.photoUrl[0])
            .centerCrop()
            .into(holder.imgRoom)

        holder.hotelName.text = room.name
    }

    override fun getItemCount(): Int = rooms.size
}

//class RoomAdapter(
//    private val mActivity: Activity, private val myList: List<RoomDetailModel>
//) : ArrayAdapter<RoomDetailModel>(mActivity, R.layout.partner_property_list_item, myList) {
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val inflater = mActivity.layoutInflater
//        val rowView: View = inflater.inflate(R.layout.partner_property_list_item, null, true)
//
//        val img: ImageView = rowView.findViewById(R.id.imgProperty)
//        val propertyTitle: TextView = rowView.findViewById(R.id.propertyName)
//
//        val room = myList[position]
//
//        Glide.with(mActivity)
//            .load(room.photoUrl[0])
//            .centerCrop()
//            .into(img)
//
//        propertyTitle.text = room.name
//
//        return rowView
//    }
//}