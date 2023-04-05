package com.example.stayfinder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubBookingDetailImageList.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyGridAdapter (private var context: Context, private var items:
ArrayList<URL>) : BaseAdapter(){
    private class ViewHolder(row: View?) {

        var logo: ImageView? = null
        init {
            logo = row?.findViewById<ImageView>(R.id.logo)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            view = (inflater as LayoutInflater).inflate(R.layout.activity_booking_detail_2_custom_gridview, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.logo?.let {
            Glide.with(context)
                .load(items[position])
                .apply(RequestOptions().centerCrop())
                .into(it)
        }


        return view as View
    }
    override fun getItem(i: Int): URL {
        return items[i]
    }
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    override fun getCount(): Int {
        return items.size
    }
}
class SubBookingDetailImageList : Fragment() {
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
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_sub_booking_detail_image_list, container, false)
        val grid = view!!.findViewById<GridView>(R.id.gridview)
        val listURL =arrayListOf(
            URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),
            URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),
            URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),
            URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),
            URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg")
        )
        val adapter  =  MyGridAdapter(this.requireContext(), listURL )
        grid.adapter = adapter
        grid.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this.requireContext(),BookingDetailActivity2::class.java)
            intent.putExtra("fragment_type","image");
            intent.putExtra("type","url");
            intent.putExtra("URL_path",(listURL[i].toString()))
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
         * @return A new instance of fragment SubBookingDetailImageList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubBookingDetailImageList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}