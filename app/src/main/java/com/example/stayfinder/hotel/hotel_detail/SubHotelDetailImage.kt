package com.example.stayfinder.hotel.hotel_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
//        val bookingDetail :HotelDetails = this.getArguments()?.getSerializable("BookingDetail") as HotelDetails

        val photoList = this.arguments?.getStringArrayList("photo_hotels")
        val hotelName = this.arguments?.getString("hotel_name")

//        for( i in bookingDetail.facilities){
//            a.add(Service(i))
//        }

        val view: View? = inflater.inflate(R.layout.fragment_hotel_detail_image_service_title, container, false)

        val textview = view!!.findViewById<TextView>(R.id.titleTv)
        val imageLayout = view.findViewById<ConstraintLayout>(R.id.imageLayout)

//        val recyclerView = view.findViewById(R.id.recyclerViewService) as RecyclerView

        textview.text = hotelName
        val img1 = view.findViewById<ImageView>(R.id.imageView1)
        val img2 = view.findViewById<ImageView>(R.id.imageView2)
        val img3 = view.findViewById<ImageView>(R.id.imageView3)
        val img4 = view.findViewById<ImageView>(R.id.imageView4)
        val img5 = view.findViewById<ImageView>(R.id.imageView5)
        val noImageET = view.findViewById<TextView>(R.id.noImageET)

//        val horizontalLayoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.layoutManager = horizontalLayoutManager
//        recyclerView.adapter = ServiceAdapter(a)

        when (photoList?.size) {
            0 -> {
                noImageET.text = ""
            }
            1 -> {
                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img1)
                noImageET.text = ""
            }
            2 -> {
                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img1)

                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img2)
                noImageET.text = ""
            }
            3 -> {
                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img1)

                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img2)

                Glide.with(this)
                    .load(photoList[2])
                    .centerCrop()
                    .into(img3)

                noImageET.text = ""
            }
            4 -> {
                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img1)

                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img2)

                Glide.with(this)
                    .load(photoList[2])
                    .centerCrop()
                    .into(img3)

                Glide.with(this)
                    .load(photoList[3])
                    .centerCrop()
                    .into(img4)

                noImageET.setText("")
            }
            5 -> {
                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img1)

                Glide.with(this)
                    .load(photoList[0])
                    .centerCrop()
                    .into(img2)

                Glide.with(this)
                    .load(photoList[2])
                    .centerCrop()
                    .into(img3)

                Glide.with(this)
                    .load(photoList[3])
                    .centerCrop()
                    .into(img4)

                Glide.with(this)
                    .load(photoList[4])
                    .centerCrop()
                    .into(img5)

                noImageET.text = ""
            }
            else -> {
                Glide.with(this)
                    .load(photoList?.get(0))
                    .centerCrop()
                    .into(img1)

                Glide.with(this)
                    .load(photoList?.get(0))
                    .centerCrop()
                    .into(img2)

                Glide.with(this)
                    .load(photoList?.get(2))
                    .centerCrop()
                    .into(img3)

                Glide.with(this)
                    .load(photoList?.get(3))
                    .centerCrop()
                    .into(img4)

                Glide.with(this)
                    .load(photoList?.get(4))
                    .centerCrop()
                    .into(img5)
                noImageET.text = "+"+(photoList!!.size - 5).toString()
            }
        }

        imageLayout.setOnClickListener{
            val intent = Intent(this.context, HotelDetailActivity2::class.java)
            intent.putExtra("fragment_type","image");
//            intent.putExtra("booking_id",bookingDetail.id);
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