package com.example.stayfinder.partner.property.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
import com.example.stayfinder.hotel.hotel_detail.MyGridAdapter
import java.net.URL

data class EditImage(
    var id: String = "",
    var name: String = "",
    var photoURL: ArrayList<String> = arrayListOf<String>(),
)
class EditImageAdapter (private var context: Context, private var items:
ArrayList<EditImage>) : BaseAdapter(){
    private class ViewHolder(row: View?) {

        var nameTv: TextView? = null
        var addBtn : Button? = null
        var deleteBtn: Button? = null
        var recyclerView: GridView? = null
        init {
            nameTv = row?.findViewById<TextView>(R.id.nameTv)
            addBtn = row?. findViewById(R.id.addBtn)
            deleteBtn = row?.findViewById(R.id.deleteBtn)
            recyclerView = row?.findViewById(R.id.gridview)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            view = (inflater as LayoutInflater).inflate(R.layout.edit_image_adapter, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.nameTv?.setText(items[position].name)

        val adapter  =  MyGridAdapter(context, items[position].photoURL )
        viewHolder.recyclerView?.adapter = adapter
        return view as View
    }
    override fun getItem(i: Int): EditImage {
        return items[i]
    }
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    override fun getCount(): Int {
        return items.size
    }
}
