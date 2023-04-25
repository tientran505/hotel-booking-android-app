package com.example.stayfinder.partner.property

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.adapter.Property
import com.example.stayfinder.partner.property.adapter.PropertyAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerPropertiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerPropertiesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var propertyLV: ListView

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
        val view = inflater.inflate(R.layout.partner_fragment_properties, container, false)

        initLV(view)

        return view
    }

    private fun initLV(view: View) {
        propertyLV = view.findViewById(R.id.propertyPartnerLV)

        val urlStr = "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80"

        val propertyList = listOf<Property>(
            Property(urlStr, "Property 1"),
            Property(urlStr, "Property 2"),
            Property(urlStr, "Property 3"),
            Property(urlStr, "Property 4"),
            Property(urlStr, "Property 5"),
            Property(urlStr, "Property 6"),
            Property(urlStr, "Property 7"),
            Property(urlStr, "Property 8"),
            Property(urlStr, "Property 9"),
            Property(urlStr, "Property 10"),
            Property(urlStr, "Property 11"),

            )

        val propertyAdapter = PropertyAdapter(requireActivity(), propertyList)
        propertyLV.adapter = propertyAdapter
        propertyLV.setOnItemClickListener { adapterView, view, i, l ->
            startActivity(Intent(requireContext(), DetailProperty::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerPropertiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerPropertiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}