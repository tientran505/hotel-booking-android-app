package com.example.stayfinder.hotel.hotel_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.*

import com.google.firebase.firestore.IgnoreExtraProperties


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HotelDetailFeedBack.newInstance] factory method to
 * create an instance of this fragment.
 */

@IgnoreExtraProperties

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
        val listReview  = (activity as HotelDetailActivity2?)?.getReview() as ArrayList<Review>
        println("listReview"+listReview)
        val feedBacks :ArrayList<FeedBack> = ArrayList<FeedBack>()
        for( i in listReview){
            feedBacks.add(FeedBack(i))
        }
        println("feedback" +feedBacks)
        val hotel_rating =getArguments()?.getSerializable("rating") as  HashMap<String, Int>
        val textView = view!!.findViewById<TextView>(R.id.textView)
        val cleanBar = view!!.findViewById<ProgressBar>(R.id.cleanBar)
        val comfortBar = view!!.findViewById<ProgressBar>(R.id.comfortBar)
        val locationBar = view!!.findViewById<ProgressBar>(R.id.locationBar)
        val serviceBar = view!!.findViewById<ProgressBar>(R.id.servicesBar)

        val cleanTV = view.findViewById<TextView>(R.id.cleanlinessRate)
        val comfortTV = view.findViewById<TextView>(R.id.comfortRate)
        val servicesTV = view.findViewById<TextView>(R.id.servicesRate)
        val locationTV = view.findViewById<TextView>(R.id.locationRate)

        cleanTV.text = hotel_rating["cleanliness"].toString()
        comfortTV.text = hotel_rating["comfort"].toString()
        servicesTV.text = hotel_rating["services"].toString()
        locationTV.text = hotel_rating["location"].toString()

        cleanBar.progress = (hotel_rating["cleanliness"]!!*20)
        comfortBar.progress = (hotel_rating["comfort"]!!*20)
        locationBar.progress = (hotel_rating["services"]!!*20)
        serviceBar.progress = (hotel_rating["location"]!!*20)
        textView.text = "Customer reviews about the place "

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