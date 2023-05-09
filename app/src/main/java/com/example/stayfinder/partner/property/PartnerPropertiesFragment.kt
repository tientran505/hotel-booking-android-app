package com.example.stayfinder.partner.property

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.stayfinder.R
import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.PartnerMainActivity
import com.example.stayfinder.partner.property.adapter.Property
import com.example.stayfinder.partner.property.adapter.PropertyAdapter
import com.example.stayfinder.partner.room.adapter.ListRoomModel
import com.example.stayfinder.services.hotel.AddHotelActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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

    private var db: FirebaseFirestore? = null
    private var collectionName :String? = null

    private lateinit var propertyLV: ListView
    var hotelList:ArrayList<Property> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        collectionName = getString(R.string.hotel_collection_name)

        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.partner_fragment_properties, container, false)

        requestListHotel(view)

        return view
    }

    private fun requestListHotel(view: View){

        val urlStr = "https://images.unsplash.com/photo-1625244724120-1fd1d34d00f6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80"

        db!!.collection(collectionName!!)
            .get()
            .addOnSuccessListener {
                    documents ->
                for(document in documents){
                    val hotel = document.toObject(HotelDetailModel::class.java)
                    var roomModel = Property(uuidHotel = hotel.id!!,
                        propertyName = hotel.hotel_name,
                        imgUrl = if(hotel.photoUrl.size > 0) hotel.photoUrl[0] else urlStr,
                        address = hotel.address.get("number") + "," + hotel.address.get("street") + ","
                                + hotel.address.get("district") + "," + hotel.address.get("ward") + ","
                                + hotel.address.get("city")
                    )
                    hotelList.add(roomModel)
                }
                initLV(view, hotelList)
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "Have error, please try again", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), PartnerMainActivity::class.java)
                startActivity(intent)
            }


    }

    private fun initLV(view: View, hotelList:ArrayList<Property>) {
        propertyLV = view.findViewById(R.id.propertyPartnerLV)

        val propertyList = hotelList

        val propertyAdapter = PropertyAdapter(requireActivity(), propertyList)
        propertyLV.adapter = propertyAdapter
        propertyLV.setOnItemClickListener { adapterView, view, i, l ->
            var itemIdHotel = hotelList[i].uuidHotel
            val intent = Intent(requireContext(), DetailProperty::class.java)
            intent.putExtra("uuidHotel", itemIdHotel)
            intent.putExtra("hotel", hotelList[i])
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        var addBtn = view.findViewById<Button>(R.id.addPropertyBtn)
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