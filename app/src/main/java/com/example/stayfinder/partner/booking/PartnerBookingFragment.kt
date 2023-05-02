package com.example.stayfinder.partner.booking

import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.BookingAdapter
import com.example.stayfinder.R
import com.example.stayfinder.partner.booking.adapter.Booking
import com.example.stayfinder.partner.booking.adapter.PartnerBookingAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerBookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerBookingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bookingRV: RecyclerView
    private lateinit var bookingAdapter: PartnerBookingAdapter

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
        val view = inflater.inflate(R.layout.partner_fragment_booking, container, false)
        initRV(view)
        return view
    }

    private fun initRV(view: View) {
        bookingRV = view.findViewById(R.id.bookingRV)
        bookingRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        val mList = arrayListOf(
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
            2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Abishai victor mad a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                6, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Hai Chu Tran's reservation has beed canceled", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", false, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
            Booking("Luong Diem made a reservation", "Thanh Huỳnh", "Saturday, April 29, 2023",
                2, "One-bedroom Apartment", true, "VND 200,000"),
        )

        bookingAdapter = PartnerBookingAdapter(mList)
        bookingRV.adapter = bookingAdapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerBookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerBookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}