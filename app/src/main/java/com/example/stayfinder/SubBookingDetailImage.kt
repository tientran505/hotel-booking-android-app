package com.example.stayfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubBookingDetailImage.newInstance] factory method to
 * create an instance of this fragment.
 */

class SubBookingDetailImage : Fragment() {
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
        val bookingDetail :bookingDetail? = this.getArguments()?.getSerializable("BookingDetail") as bookingDetail?
        val view: View? = inflater.inflate(R.layout.fragment_booking_detail_image_title, container, false)
        var textview = view!!.findViewById<TextView>(R.id.titleTv)
        textview.setText(bookingDetail?.titlename)
        var img1 = view!!.findViewById<ImageView>(R.id.imageView1)
        var img2 = view!!.findViewById<ImageView>(R.id.imageView2)
        var img3 = view!!.findViewById<ImageView>(R.id.imageView3)
        var img4 = view!!.findViewById<ImageView>(R.id.imageView4)
        var img5 = view!!.findViewById<ImageView>(R.id.imageView5)
        var noImageET = view!!.findViewById<TextView>(R.id.noImageET)
        when (bookingDetail?.img?.size!!) {
            0 -> {

            }
            1 -> {
                img1.setImageResource(bookingDetail.img[0])
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop)
                        .into(img1)
                }
                noImageET.setText("")
            }
            2 -> {
                img1.setImageResource(bookingDetail.img[0])
                img2.setImageResource(bookingDetail.img[1])
                noImageET.setText("")
            }
            3 -> {
                img1.setImageResource(bookingDetail.img[0])
                img2.setImageResource(bookingDetail.img[1])
                img3.setImageResource(bookingDetail.img[2])
                noImageET.setText("")
            }
            4 -> {
                img1.setImageResource(bookingDetail.img[0])
                img2.setImageResource(bookingDetail.img[1])
                img3.setImageResource(bookingDetail.img[2])
                img4.setImageResource(bookingDetail.img[3])
                noImageET.setText("")
            }
            5 -> {
                img1.setImageResource(bookingDetail.img[0])
                img2.setImageResource(bookingDetail.img[1])
                img3.setImageResource(bookingDetail.img[2])
                img4.setImageResource(bookingDetail.img[3])
                img5.setImageResource(bookingDetail.img[4])
                noImageET.setText("")
            }
            else -> {
                img1.setImageResource(bookingDetail.img[0])
                img2.setImageResource(bookingDetail.img[1])
                img3.setImageResource(bookingDetail.img[2])
                img4.setImageResource(bookingDetail.img[3])
                img5.setImageResource(bookingDetail.img[4])
                noImageET.setText("+"+(bookingDetail.img.size - 5).toString())
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
         * @return A new instance of fragment subBookingDetail1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubBookingDetailImage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}