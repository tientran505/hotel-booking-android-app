package com.example.stayfinder.hotel.hotel_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
import com.example.stayfinder.Service
import com.example.stayfinder.ServiceAdapter
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubHotelDetailImage.newInstance] factory method to
 * create an instance of this fragment.
 */


class SubHotelDetailImage : Fragment() {
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
        val a = arrayListOf<Service>()
        val bookingDetail :HotelDetails = this.getArguments()?.getSerializable("BookingDetail") as HotelDetails

        for( i in bookingDetail.facilities){
            a.add(Service(i))
        }
        val view: View? = inflater.inflate(R.layout.fragment_hotel_detail_image_service_title, container, false)
        var textview = view!!.findViewById<TextView>(R.id.titleTv)
        val imageLayout = view!!.findViewById<ConstraintLayout>(R.id.imageLayout)
        var recyclerView = view!!.findViewById(R.id.recyclerViewService) as RecyclerView
        textview.setText(bookingDetail?.hotel_name)
        var img1 = view!!.findViewById<ImageView>(R.id.imageView1)
        var img2 = view!!.findViewById<ImageView>(R.id.imageView2)
        var img3 = view!!.findViewById<ImageView>(R.id.imageView3)
        var img4 = view!!.findViewById<ImageView>(R.id.imageView4)
        var img5 = view!!.findViewById<ImageView>(R.id.imageView5)
        var noImageET = view!!.findViewById<TextView>(R.id.noImageET)
        val horizontalLayoutManagaer = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManagaer
        recyclerView.adapter = ServiceAdapter(a)
        when (bookingDetail?.img?.size!!) {
            0 -> {
                noImageET.setText("")
            }
            1 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[0]))
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                noImageET.setText("")
            }
            2 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[0]))
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[1]))
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                noImageET.setText("")
            }
            3 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[0]))
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[1]))
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[2]))
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }

                noImageET.setText("")
            }
            4 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[0]))
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[1]))
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[2]))
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                if(bookingDetail.img[3] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[3]))
                        .apply(RequestOptions().centerCrop())
                        .into(img4)
                }

                noImageET.setText("")
            }
            5 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[0]))
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[1]))
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[3]))
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                if(bookingDetail.img[3] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[4]))
                        .apply(RequestOptions().centerCrop())
                        .into(img4)
                }
                if(bookingDetail.img[4] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[4]))
                        .apply(RequestOptions().centerCrop())
                        .into(img5)
                }
                noImageET.setText("")
            }
            else -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[0]))
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[1]))
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[3]))
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                if(bookingDetail.img[3] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[4]))
                        .apply(RequestOptions().centerCrop())
                        .into(img4)
                }
                if(bookingDetail.img[4] != null){
                    Glide.with(this)
                        .load(URL(bookingDetail.img[4]))
                        .apply(RequestOptions().centerCrop())
                        .into(img5)
                }
                noImageET.setText("+"+(bookingDetail.img.size - 5).toString())
            }
        }
        imageLayout.setOnClickListener{
            val intent = Intent(this.context, HotelDetailActivity2::class.java)
            intent.putExtra("fragment_type","image");
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
         * @return A new instance of fragment subBookingDetail1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubHotelDetailImage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}