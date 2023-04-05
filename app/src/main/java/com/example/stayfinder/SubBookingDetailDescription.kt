package com.example.stayfinder

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.ms.square.android.expandabletextview.ExpandableTextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubBookingDetailDescription.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubBookingDetailDescription : Fragment() {
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
        val bookingDetail :Hotel ? = this.getArguments()?.getSerializable("BookingDetail") as Hotel ?
        val view: View? = inflater.inflate(R.layout.fragment_sub_booking_detail_description, container, false)
        val expendTv = view!!.findViewById<ExpandableTextView>(R.id.expand_text_view)
        expendTv.setText(bookingDetail!!.descript )
        val ratingTv = view!!.findViewById<TextView>(R.id.ratingTv)
        ratingTv.setText(bookingDetail.rating.toString())
        val EvaluateTv= view!!.findViewById<TextView>(R.id.EvaluateTv)
        if(bookingDetail.rating!! < 1.0){
            EvaluateTv.setText("Very Poor")
        }
        else if(bookingDetail.rating!! <2.0){
            EvaluateTv.setText("Very Poor")
        }
        else if (bookingDetail.rating!! <3.0){
            EvaluateTv.setText("Average")

        }
        else if (bookingDetail.rating!! <4.0){
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
            val intent = Intent(this.context, BookingDetailActivity2::class.java)
            intent.putExtra("fragment_type","feebback");
            intent.putExtra("booking_id",bookingDetail.id);
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
            SubBookingDetailDescription().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}