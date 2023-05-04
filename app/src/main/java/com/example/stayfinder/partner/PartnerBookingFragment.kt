package com.example.stayfinder.partner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.Booking
import com.example.stayfinder.BookingAdapter
import com.example.stayfinder.R
import java.net.URL

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

    var listbooking  = ArrayList<Booking>()

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
        var view: View? = null
        view = inflater.inflate(R.layout.partner_fragment_booking, container, false)

        listbooking?.add(
            Booking("The Sóng Vũng Tàu Homestay- Vũng Tàu Land 1","4 Feb","5 Feb",2000000.0,"Waiting",
                URL("https://www.google.com/imgres?imgurl=https%3A%2F%2Fpix10.agoda.net%2FhotelImages%2F124%2F1246280%2F1246280_16061017110043391702.jpg%3Fca%3D6%26ce%3D1%26s%3D1024x768&tbnid=6v7Euel4ecy_8M&vet=12ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ..i&imgrefurl=https%3A%2F%2Fwww.agoda.com%2Fvi-vn%2Fl-hotel%2Fhotel%2Fkhon-kaen-th.html&docid=mssDHIF707HKHM&w=1024&h=768&q=hotel%20image&ved=2ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ")
            )
        )
        listbooking?.add(
            Booking("The Sóng Vũng Tàu Homestay- Vũng Tàu Land 2","4 Feb","5 Feb",2000000.0,"Accept",
                URL("https://www.google.com/imgres?imgurl=https%3A%2F%2Fpix10.agoda.net%2FhotelImages%2F124%2F1246280%2F1246280_16061017110043391702.jpg%3Fca%3D6%26ce%3D1%26s%3D1024x768&tbnid=6v7Euel4ecy_8M&vet=12ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ..i&imgrefurl=https%3A%2F%2Fwww.agoda.com%2Fvi-vn%2Fl-hotel%2Fhotel%2Fkhon-kaen-th.html&docid=mssDHIF707HKHM&w=1024&h=768&q=hotel%20image&ved=2ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ")
            )
        )
        listbooking?.add(
            Booking("The Sóng Vũng Tàu Homestay- Vũng Tàu Land 3","4 Feb","5 Feb",2000000.0,"Accept",
                URL("https://www.google.com/imgres?imgurl=https%3A%2F%2Fpix10.agoda.net%2FhotelImages%2F124%2F1246280%2F1246280_16061017110043391702.jpg%3Fca%3D6%26ce%3D1%26s%3D1024x768&tbnid=6v7Euel4ecy_8M&vet=12ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ..i&imgrefurl=https%3A%2F%2Fwww.agoda.com%2Fvi-vn%2Fl-hotel%2Fhotel%2Fkhon-kaen-th.html&docid=mssDHIF707HKHM&w=1024&h=768&q=hotel%20image&ved=2ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ")
            )
        )
        listbooking?.add(
            Booking("The Sóng Vũng Tàu Homestay- Vũng Tàu Land 4","4 Feb","5 Feb",2000000.0,"Discard",
                URL("https://www.google.com/imgres?imgurl=https%3A%2F%2Fpix10.agoda.net%2FhotelImages%2F124%2F1246280%2F1246280_16061017110043391702.jpg%3Fca%3D6%26ce%3D1%26s%3D1024x768&tbnid=6v7Euel4ecy_8M&vet=12ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ..i&imgrefurl=https%3A%2F%2Fwww.agoda.com%2Fvi-vn%2Fl-hotel%2Fhotel%2Fkhon-kaen-th.html&docid=mssDHIF707HKHM&w=1024&h=768&q=hotel%20image&ved=2ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ")
            )
        )
        listbooking?.add(
            Booking("The Sóng Vũng Tàu Homestay- Vũng Tàu Land 5","4 Feb","5 Feb",2000000.0,"Discard",
                URL("https://www.google.com/imgres?imgurl=https%3A%2F%2Fpix10.agoda.net%2FhotelImages%2F124%2F1246280%2F1246280_16061017110043391702.jpg%3Fca%3D6%26ce%3D1%26s%3D1024x768&tbnid=6v7Euel4ecy_8M&vet=12ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ..i&imgrefurl=https%3A%2F%2Fwww.agoda.com%2Fvi-vn%2Fl-hotel%2Fhotel%2Fkhon-kaen-th.html&docid=mssDHIF707HKHM&w=1024&h=768&q=hotel%20image&ved=2ahUKEwiDkN6e5ov-AhVdDbcAHQTPDKkQMygAegUIARCyAQ")
            )
        )

        var waitingBtn = view.findViewById<Button>(R.id.waitingBtn)
        var acceptBtn = view.findViewById<Button>(R.id.acceptBtn)
        var discardBtn = view.findViewById<Button>(R.id.discardBtn)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter = BookingAdapter(listbooking)

        var ListDefault: ArrayList<Booking> = listbooking.filter { it.status == "Waiting" } as ArrayList<Booking>
        adapter.updateList(ListDefault)

        waitingBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
        acceptBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
        discardBtn!!.setBackgroundResource(R.drawable.custom_button_booking)

        adapter.updateList(ListDefault)
        if(ListDefault.size != 0 ){
            recyclerView?.setVisibility(View.VISIBLE)
        }
        else{
            recyclerView?.setVisibility(View.INVISIBLE)
        }
        adapter.onItemClick={position->

        }

        waitingBtn!!.setOnClickListener(){
            waitingBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
            acceptBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            discardBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            var waitingList: ArrayList<Booking> = listbooking.filter { it.status == "Waiting" } as ArrayList<Booking>

            adapter.updateList(waitingList)
            if(waitingList.size != 0 ){
                recyclerView?.setVisibility(View.VISIBLE)
            }
            else{
                recyclerView?.setVisibility(View.INVISIBLE)
            }
            adapter.onItemClick={position->

            }
        }

        acceptBtn!!.setOnClickListener(){
            acceptBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
            waitingBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            discardBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            var acceptList: ArrayList<Booking> = listbooking.filter { it.status == "Accept" } as ArrayList<Booking>

            adapter.updateList(acceptList)
            if(acceptList.size != 0 ){
                recyclerView?.setVisibility(View.VISIBLE)
            }
            else{
                recyclerView?.setVisibility(View.INVISIBLE)
            }
            adapter.onItemClick={position->

            }
        }

        discardBtn!!.setOnClickListener(){
            discardBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
            acceptBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            waitingBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            var discardList: ArrayList<Booking> = listbooking.filter { it.status == "Discard" } as ArrayList<Booking>

            adapter.updateList(discardList)
            if(discardList.size != 0 ){
                recyclerView?.setVisibility(View.VISIBLE)
            }
            else{
                recyclerView?.setVisibility(View.INVISIBLE)
            }
            adapter.onItemClick={position->

            }
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