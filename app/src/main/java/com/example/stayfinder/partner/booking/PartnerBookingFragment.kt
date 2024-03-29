package com.example.stayfinder.partner.booking

import android.content.Intent
import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.BookingAdapter
import com.example.stayfinder.BookingDetail
import com.example.stayfinder.DetailListActivity
import com.example.stayfinder.R
import com.example.stayfinder.partner.booking.adapter.Booking
import com.example.stayfinder.partner.booking.adapter.PartnerBookingAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    val db = Firebase.firestore

    private val mList = arrayListOf<BookingDetail>()


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
        fetchData()
        return view
    }

    private fun fetchData() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        db.collection("bookings").whereEqualTo("hotel.owner_id", user?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val bookingDetail = document.toObject(BookingDetail::class.java)

                    mList.add(bookingDetail)
                    bookingAdapter.notifyItemInserted(mList.size - 1)
                }
            }
    }

    private fun initRV(view: View) {
        bookingRV = view.findViewById(R.id.bookingRV)
        bookingRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        bookingAdapter = PartnerBookingAdapter(mList)
        bookingRV.adapter = bookingAdapter
        bookingAdapter.setOnItemClickListener(object: PartnerBookingAdapter.onItemClickListener{
            override fun onItemClick(position: Int){
                val intent = Intent(activity, PartnerBookingDetail::class.java)
                intent.putExtra("booking_id",mList[position].id)
                startActivity(intent)
            }
        })
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