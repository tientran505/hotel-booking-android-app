package com.example.stayfinder.hotel.hotel_detail
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
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
ArrayList<String>) : BaseAdapter(){
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
            view = (inflater as LayoutInflater).inflate(R.layout.activity_hotel_detail_2_custom_gridview, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.logo?.let {
            Glide.with(context)
                .load(URL(items[position]))
                .apply(RequestOptions().centerCrop())
                .into(it)
        }


        return view as View
    }
    override fun getItem(i: Int): String {
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
        val view: View = inflater.inflate(R.layout.fragment_sub_hotel_detail_image_list, container, false)
        val grid = view!!.findViewById<GridView>(R.id.gridview)
//        val listImage = this.getArguments()?.getSerializable("listImage") as ArrayList<String>
        val listImage  = (activity as HotelDetailActivity2?)?.getImages() ?: ArrayList<String>()
        val adapter  =  MyGridAdapter(this.requireContext(), listImage )
        grid.adapter = adapter
        grid.setOnItemClickListener { adapterView, view, i, l ->
            val directImageFragment = HotelDetailImageDirect()
            val bundle = Bundle()
            bundle.putInt("position",i)
            directImageFragment.setArguments(bundle);
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, directImageFragment)
                .addToBackStack(null)
                .commit()
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