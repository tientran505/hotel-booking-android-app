package com.example.stayfinder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HotelDetailImageDirect.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPagerAdapter() : PagerAdapter() {
    var context: Context? = null
    var images = ArrayList<URL>()
    var layoutInflater: LayoutInflater? = null

    constructor(images: ArrayList<URL>, context: Context) : this() {
        this.images = images
        this.context = context
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as ConstraintLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater?.inflate(R.layout.image_item, container, false)
        val imageView = itemView!!.findViewById<ImageView>(R.id.imageView)
        context?.let {
            if (imageView != null) {
                Glide.with(it)
                    .load(images[position])
                    .apply(RequestOptions().centerCrop())
                    .into(imageView)
            }
        }

        container.addView(itemView)
        return itemView!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
class HotelDetailImageDirect : Fragment() {
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
    var viewpager: ViewPager? = null
    var pageAdapter: ViewPagerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_sub_hotel_detail_image_direct, container, false)
//        var imageView: ImageView?= null
//        imageView = view!!.findViewById(R.id.imageView)
//        val path :String? = this.arguments?.getString("url_path") as String
//        Glide.with(this)
//            .load(URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"))
//            .apply(RequestOptions().centerCrop())
//            .into(imageView)

        var images = arrayListOf<URL>(URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),URL("https://i.pinimg.com/564x/d7/63/ac/d763ac7553412d6518022b13bc1c315d.jpg"),URL("https://i.pinimg.com/564x/ae/c7/4e/aec74e5d0c601fc617174f44f9fab236.jpg"))
        viewpager = view!!.findViewById(R.id.viewPager)
        val position =this.arguments?.getInt("position") as Int
        pageAdapter = ViewPagerAdapter(images,this.requireContext())
        viewpager?.adapter = pageAdapter
        println(position.toString())
        viewpager?.setCurrentItem(position);

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubBookingDetailImageDirect.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HotelDetailImageDirect().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}