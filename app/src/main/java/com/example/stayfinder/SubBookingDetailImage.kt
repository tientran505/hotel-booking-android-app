package com.example.stayfinder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL


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
        val a = arrayListOf<Service>(Service("wifi", URL("https://cdn-icons-png.flaticon.com/512/93/93158.png")),Service("wifi", URL("https://cdn-icons-png.flaticon.com/512/93/93158.png")),Service("wifi", URL("https://cdn-icons-png.flaticon.com/512/93/93158.png")))
        val bookingDetail :Hotel ? = this.getArguments()?.getSerializable("BookingDetail") as Hotel ?
        val view: View? = inflater.inflate(R.layout.fragment_booking_detail_image_service_title, container, false)
        var textview = view!!.findViewById<TextView>(R.id.titleTv)
        var recyclerView = view!!.findViewById(R.id.recyclerViewService) as RecyclerView
        textview.setText(bookingDetail?.titlename)
        var img1 = view!!.findViewById<ImageView>(R.id.imageView1)
        var img2 = view!!.findViewById<ImageView>(R.id.imageView2)
        var img3 = view!!.findViewById<ImageView>(R.id.imageView3)
        var img4 = view!!.findViewById<ImageView>(R.id.imageView4)
        var img5 = view!!.findViewById<ImageView>(R.id.imageView5)
        var noImageET = view!!.findViewById<TextView>(R.id.noImageET)
        print((bookingDetail?.img?.size?.minus(5)).toString())
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
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                noImageET.setText("")
            }
            2 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                noImageET.setText("")
            }
            3 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                noImageET.setText("")
            }
            4 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                if(bookingDetail.img[3] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img4)
                }
                noImageET.setText("")
            }
            5 -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                if(bookingDetail.img[3] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img4)
                }
                if(bookingDetail.img[4] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img5)
                }
                noImageET.setText("")
            }
            else -> {
                if(bookingDetail.img[0] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img1)
                }
                if(bookingDetail.img[1] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img2)
                }
                if(bookingDetail.img[2] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img3)
                }
                if(bookingDetail.img[3] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img4)
                }
                if(bookingDetail.img[4] != null){
                    Glide.with(this)
                        .load(bookingDetail.img[0])
                        .apply(RequestOptions().centerCrop())
                        .into(img5)
                }

                noImageET.setText("+"+(bookingDetail.img.size - 5).toString())
            }
        }
        img1.setOnClickListener{
            val intent = Intent(this.context, BookingDetailActivity2::class.java)
            intent.putExtra("fragment_type","image");
            intent.putExtra("booking_id",bookingDetail.id);
            intent.putExtra("type","grid")
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
            SubBookingDetailImage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}