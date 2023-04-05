package com.example.stayfinder.city

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R

class City(val name: String)

class CityAdapter(
    private val cities: List<City>,

) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    private var selectedPosition = 0 // RecyclerView.NO_POSITION
    var onItemClick: ((Int) -> Unit)? = null

     fun setSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = cities.size

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val cityNameBtn: Button = listItemView.findViewById(R.id.cityBtn)

        init {
            cityNameBtn.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        holder.cityNameBtn.text = city.name

        val hexPrimaryColor: String = String.format("#%06X", (0xFFFFFF and R.color.primary))
        if (position == selectedPosition) {
            holder.cityNameBtn.setTextColor(Color.parseColor("#FFFFFF"))
            holder.cityNameBtn.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(hexPrimaryColor))
        } else {
            holder.cityNameBtn.setTextColor(Color.parseColor(hexPrimaryColor))
            holder.cityNameBtn.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#DCD6D6"))
        }
    }
}