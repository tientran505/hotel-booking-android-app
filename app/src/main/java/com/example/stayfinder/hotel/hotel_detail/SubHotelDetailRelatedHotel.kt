package com.example.stayfinder.hotel.hotel_detail

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.hotel.HotelAdapter
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.rating
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL
import java.text.NumberFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubHotelDetailRelatedHotel.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotelRelate(val hotel_id: String, val hotelName: String, val rating: Double,val img: String,
            var isSaved: Boolean){
}
fun checknullDouble(a: Double?): Double{
    if(a == null) return  0.0
    else return a
}

class HotelRelatedAdapter (private var item: ArrayList<HotelRelate>) : RecyclerView.Adapter<HotelRelatedAdapter.ViewHolder>() {
    var onClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val rating: RatingBar = listItemView.findViewById(R.id.hotelRB)
        val hotelName: TextView = listItemView.findViewById(R.id.hotelNameTV)
        val hotelIV: ImageView = listItemView.findViewById(R.id.hotelIV)
        var card: ConstraintLayout = listItemView.findViewById(R.id.card)
        init {

            listItemView.setOnClickListener {
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelRelatedAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.hotel_relate_item, parent, false)
        return ViewHolder(contactView)
    }
    override fun onBindViewHolder(holder: HotelRelatedAdapter.ViewHolder, position: Int) {
        holder.hotelIV?.let {
            Glide.with(holder.itemView.context)
                .load(URL(item[position].img))
                .apply(RequestOptions().centerCrop())
                .into(it)
        }
        holder.hotelName.setText(item[position].hotelName)
        holder.rating.setRating(this.item[position].rating.toFloat());
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<HotelRelate>){
        this.item = list
        this.notifyDataSetChanged()
    }
}

class SubHotelDetailRelatedHotel : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var rvHotel: RecyclerView
    private lateinit var hotelAdapter: HotelRelatedAdapter
    var hotelslist : ArrayList<HotelRelate> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_sub_hotel_detail_related_hotel, container, false)
//        val city = "Thành phố Hồ Chí Minh"
        val city = this.arguments?.getString("city") as String
        val hotel_id = requireArguments().getString("hotel_id")
        println("city "+ city)
        rvHotel= view!!.findViewById(R.id.realteHotelRecyclerView)
        rvHotel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hotelAdapter = HotelRelatedAdapter(hotelslist)
        rvHotel.adapter =hotelAdapter
        FirebaseFirestore.getInstance().collection("hotels")
            .whereEqualTo("address.city", city).limit(6)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if(document.get("id") != hotel_id){
                        val hotel = document.toObject(HotelDetailModel::class.java) as HotelDetailModel
//                    if(hotel != null)
                        hotelslist.add(HotelRelate(hotel.id!!, hotel.hotel_name!!, hotel.rating_overall!!.toDouble(),hotel.photoUrl[0].toString(),false))
//                    println("HotelRelate(hotel)"+HotelRelate(hotel).size)
                        println("hotelslist"+hotelslist.size)
//                    hotelAdapter.notifyItemChanged(hotelslist.size-1)
                    }

                }

                hotelAdapter.updateList(hotelslist)
                println(":hotelAdapter.getItemCount()"+hotelAdapter.getItemCount().toString())
            }
            .addOnFailureListener { exception ->
            }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubHotelDetailRelatedHotel.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubHotelDetailRelatedHotel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
