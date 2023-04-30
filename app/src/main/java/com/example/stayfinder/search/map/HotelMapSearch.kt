package com.example.stayfinder.search.map

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.stayfinder.R
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.hotel.hotel_detail.HotelDetailActivity
import com.example.stayfinder.saved.choose_item.SavedListChooseBottomSheetDialog
import com.example.stayfinder.search.Photo
import com.example.stayfinder.search.PhotoAdapter
import me.relex.circleindicator.CircleIndicator3
import java.text.NumberFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HotelMapSearch.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotelMapSearch(val hotel: Hotel) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var heartBtn: ImageButton
    private lateinit var rating: RatingBar
    private lateinit var originalPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var hotelName: TextView
    private lateinit var hotelVP: ViewPager2
    private lateinit var indicator: CircleIndicator3
    private lateinit var cityName: TextView
    private lateinit var card: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.hotel_search_list_item, container, false)
        initComponent(view)
        bindingComponents()

        view.setOnClickListener {
            val intent = Intent(requireContext(), HotelDetailActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun initComponent(view: View) {
        heartBtn = view.findViewById(R.id.heartBtn)
        rating = view.findViewById(R.id.HotelSearchRB)
        originalPrice = view.findViewById(R.id.originalPriceSearchTV)
        discountPrice = view.findViewById(R.id.discountPriceSearchTV)
        hotelName = view.findViewById(R.id.hotelNameSearchTV)
        hotelVP = view.findViewById(R.id.viewPager2)
        indicator = view.findViewById(R.id.circle_indicator)
        cityName = view.findViewById(R.id.locationSearchTV)
        card = view.findViewById(R.id.hotelSearch)


        heartBtn.setOnClickListener {
            val collectionBottomSheet = SavedListChooseBottomSheetDialog()
            collectionBottomSheet.show(requireActivity().supportFragmentManager
                , SavedListChooseBottomSheetDialog.TAG)
        }
    }

    private fun bindingComponents() {
        val photoAdapter = PhotoAdapter(requireContext(), getListPhoto())
        hotelVP.adapter = photoAdapter
        indicator.setViewPager(hotelVP)
        photoAdapter.registerAdapterDataObserver(indicator.adapterDataObserver)

        card.clipToOutline = true
        card.outlineProvider = ViewOutlineProvider.BACKGROUND
        heartBtn.setImageResource(R.drawable.add_circle_outline)
        rating.rating = hotel.rating

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vn", "VN"))

        originalPrice.text = numberFormat.format(hotel.originalPrice)
        originalPrice.paintFlags = originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        discountPrice.text = numberFormat.format(hotel.discountPrice)
        cityName.text = hotel.cityName
        hotelName.text = hotel.hotelName

    }

    private fun getListPhoto(): List<Photo> {
        return listOf(
            Photo(R.drawable.img_1),
            Photo(R.drawable.img_2),
            Photo(R.drawable.img_3),
            Photo(R.drawable.img_4),
            Photo(R.drawable.img_5),
        )
    }
}