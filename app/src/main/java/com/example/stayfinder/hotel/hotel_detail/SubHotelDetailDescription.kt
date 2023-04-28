package com.example.stayfinder.hotel.hotel_detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.stayfinder.R
import com.example.stayfinder.rating
import com.ms.square.android.expandabletextview.ExpandableTextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubHotelDetailDescription.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubHotelDetailDescription : Fragment() {
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
        val bookingDetail :HotelDetails ? = this.getArguments()?.getSerializable("BookingDetail") as HotelDetails ?
        val view: View? = inflater.inflate(R.layout.fragment_sub_hotel_detail_description, container, false)
        val expendTv = view!!.findViewById<ExpandableTextView>(R.id.expand_text_view)
        expendTv.setText(bookingDetail!!.description )
        val ratingTv = view!!.findViewById<TextView>(R.id.ratingTv)
        ratingTv.setText(bookingDetail.rating_overall.toString())
        val EvaluateTv= view!!.findViewById<TextView>(R.id.EvaluateTv)
        if(bookingDetail.rating_overall!! < 1.0){
            EvaluateTv.setText("Very Poor")
        }
        else if(bookingDetail.rating_overall!! <2.0){
            EvaluateTv.setText("Very Poor")
        }
        else if (bookingDetail.rating_overall!! <3.0){
            EvaluateTv.setText("Average")

        }
        else if (bookingDetail.rating_overall!! <4.0){
            EvaluateTv.setText("Good")
        }
        else{
            EvaluateTv.setText("Excelent")
        }
        if(bookingDetail.noFeedback!=0){
            val noFeedbackTv = view!!.findViewById<TextView>(R.id.noFeedbackTv)
            noFeedbackTv.setText(bookingDetail.noFeedback.toString()+" have leave a feedback for this place")
        }

        val FeedbackBtn = view!!.findViewById<RelativeLayout>(R.id.FeedbackBtn)
        FeedbackBtn.setOnClickListener{
            val intent = Intent(this.context, HotelDetailActivity2::class.java)
            val rating = this.getArguments()?.getSerializable("rating") as rating
            intent.putExtra("fragment_type","feedback");
            intent.putExtra("booking_id",bookingDetail.id);
            intent.putExtra("hotel_detail",bookingDetail);
            intent.putExtra("rating",rating);
            startActivity(intent)
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
         * @return A new instance of fragment SubBookingDetailDescription.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubHotelDetailDescription().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}