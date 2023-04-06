package com.example.stayfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HotelDetailFeedBack.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotelDetailFeedBack : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? =inflater.inflate(R.layout.fragment_sub_hotel_detail_feed_back, container, false)
        val feedBacks :ArrayList<FeedBack>? = arrayListOf(
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
            FeedBack("Anh Thư", URL("https://i.pinimg.com/originals/17/17/32/171732cf7fa89190d48ddb9feee8242e.jpg"),"Excellent","really happy to live here","4-4-2023",4.5,"single","30-3-2023",3,"single"),
        )

        val cleanliness = 4.5
        val service = 4.2
        val location = 4.0
        val comfort = 3.2

//        val bookingDetail :bookingDetail? = this.getArguments()?.getSerializable("BookingDetail") as bookingDetail?
//        val bookingid = this.getArguments()?.getString("booking_id")
        val textView = view!!.findViewById<TextView>(R.id.textView)
        val cleanBar = view!!.findViewById<ProgressBar>(R.id.cleanBar)
        val comfortBar = view!!.findViewById<ProgressBar>(R.id.comfortBar)
        val locationBar = view!!.findViewById<ProgressBar>(R.id.locationBar)
        val serviceBar = view!!.findViewById<ProgressBar>(R.id.servicesBar)

        cleanBar.setProgress((cleanliness*20).toInt())
        comfortBar.setProgress((comfort*20).toInt())
        locationBar.setProgress((location*20).toInt())
        serviceBar.setProgress((service*20).toInt())

//        if (bookingid != null) {
            textView.setText("Customer reviews about the place ")
//        }

        val recyclerview = view!!.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview?.layoutManager = LinearLayoutManager(this.context)
        val adapter = feedBacks?.let { FeedbackAdapter(it) }
        recyclerview?.adapter = adapter
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HotelDetailFeedBack().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}