package com.example.stayfinder.partner.property

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.example.stayfinder.R

import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity

import com.example.stayfinder.hotels
import com.example.stayfinder.partner.property.adapter.PropertyAdapter
import com.example.stayfinder.partner.room.adapter.ListRoomModel
import com.example.stayfinder.services.hotel.AddHotelActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.internal.InternalTokenProvider
import com.google.firebase.ktx.Firebase

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

    private var collectionName :String? = null

    private lateinit var propertyLV: ListView
    private val propertyList = ArrayList<hotels>()

    val db = Firebase.firestore

    private lateinit var propertyAdapter: PropertyAdapter

    private lateinit var progressBar: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        collectionName = getString(R.string.hotel_collection_name)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.partner_fragment_properties, container, false)

        progressBar = view.findViewById(R.id.progressBar)

        initLV(view)
        return view
    }


    private fun fetchData() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val docRef = db.collection("hotels").whereEqualTo("owner_id", user.uid).whereNotEqualTo("hotel_name", "")
            docRef.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val hotel = document.toObject<hotels>()
                    Log.d("tien", hotel.toString())
                    propertyList.add(hotel)
                }
                propertyAdapter.notifyDataSetChanged()

                progressBar.visibility = View.GONE
            }
                .addOnFailureListener { exception ->
                    Log.w("log", "Error getting documents: ", exception)
                }

        }
    }

    private fun initLV(view: View) {
        propertyLV = view.findViewById(R.id.propertyPartnerLV)
        fetchData()

        propertyAdapter = PropertyAdapter(requireActivity(), propertyList)

        propertyLV.adapter = propertyAdapter
        propertyLV.setOnItemClickListener { adapterView, view, i, l ->
            var itemIdHotel = propertyList[i].id
            val intent = Intent(requireContext(), DetailProperty::class.java)
            intent.putExtra("uuidHotel", itemIdHotel)
            intent.putExtra("hotel", propertyList[i])
            startActivity(intent)

            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        val addBtn = view.findViewById<Button>(R.id.addPropertyBtn)
        addBtn.setOnClickListener {
            startActivity(Intent(requireContext(), AddHotelActivity::class.java))
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