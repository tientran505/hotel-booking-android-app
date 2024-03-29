package com.example.stayfinder.partner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.PartnerHotelList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerMoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerMoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var logOutBtn: Button
    private lateinit var promotionBtn: Button

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
        val view = inflater.inflate(R.layout.partner_fragment_more, container, false)

        initComponent(view)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerMoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerMoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initComponent(view: View) {
        logOutBtn = view.findViewById(R.id.partnerLogOutBtn)

        logOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finishAffinity()
        }

        promotionBtn = view.findViewById(R.id.promotionBtn)
        promotionBtn.setOnClickListener{
            startActivity(Intent(requireActivity(), PartnerHotelList::class.java))
        }
    }
}