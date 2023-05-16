package com.example.stayfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.city.City
import com.example.stayfinder.city.CityAdapter
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.hotel.HotelAdapter
import com.example.stayfinder.search.HotelSearch
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), RoomSelectionBottomSheetDialog.BottomSheetListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var dateET: EditText? = null
    private var roomET: EditText? = null
    private var locationET: AutoCompleteTextView? = null

    private var cityList = arrayListOf<String>()

    private val db = Firebase.firestore

    private var searchBtn: Button? = null

    private lateinit var listener: RoomSelectionBottomSheetDialog.BottomSheetListener
    private lateinit var modalBottomSheet: RoomSelectionBottomSheetDialog

    private var currentRoomInformation: RoomInformation? = null

    private var start: Long = 0
    private var end: Long = 0

    private var headerText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun formValidate(): Boolean {
        if (dateET?.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields before searching", Toast.LENGTH_SHORT).show()
            return false
        }

        if (locationET?.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields before searching", Toast.LENGTH_SHORT).show()
            return false
        }

        if (roomET?.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields before searching", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun fetchCityData(view: View) {
        locationET = view.findViewById(R.id.locationET)

        db.collection("hotels")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val city = document.getString("address.city")
                    if (city != null && !cityList.contains(city)) {
                        cityList.add(city)
                    }
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cityList)
                locationET?.setAdapter(adapter)
                locationET?.threshold = 1
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initRoomSelection(view)
        fetchCityData(view)

        return view
    }

    private fun initRoomSelection(view: View) {
        listener = this
        dateET = view.findViewById(R.id.dateET)
        dateET?.setOnClickListener {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = today
            calendar.add(Calendar.MONTH, 15)

            val dateRangePicker = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setTitleText("Select a date range")
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setStart(today)
                        .setEnd(calendar.timeInMillis)
                        .setValidator(DateValidatorPointForward.now())
                        .build()
                )
                .build()


            dateRangePicker.addOnPositiveButtonClickListener {
                val startDate = it.first
                val endDate = it.second

                if (startDate != null && endDate != null) {
                    start = startDate.toLong()
                    end = endDate.toLong()

                    dateET?.setText(dateRangePicker.headerText)

                }

            }

            dateRangePicker.addOnNegativeButtonClickListener {

            }

            dateRangePicker.addOnCancelListener {

            }

            dateRangePicker.show(requireActivity().supportFragmentManager, "DatePicker")
        }
        roomET = view.findViewById(R.id.roomInfoET)
        roomET?.setOnClickListener {
            var content = ""

            if (currentRoomInformation != null) {
                content = Json.encodeToString(currentRoomInformation)
            }

            modalBottomSheet = RoomSelectionBottomSheetDialog(content, listener)
            modalBottomSheet.show(requireActivity().supportFragmentManager
                , RoomSelectionBottomSheetDialog.TAG)
        }

        searchBtn = view.findViewById(R.id.searchBtn)
        searchBtn?.setOnClickListener {
            if (formValidate()) {
                val intent = Intent(requireContext(), HotelSearch::class.java)
                val bookings = currentRoomInformation?.let { it1 ->
                    BookingInformation(
                        number_of_adult = it1.adult,
                        number_of_children = it1.children,
                        sum_people = calculateTotalPeople(it1.adult, it1.children)
                    )
                }

                intent.putExtra("booking_info", bookings)
                intent.putExtra("start_date", start)
                intent.putExtra("end_date", end)
                intent.putExtra("city", locationET?.text.toString())
                intent.putExtra("header", dateET?.text.toString())

                startActivity(intent)
            }
        }
    }

    private fun calculateTotalPeople(adults: Int, children: Int): Int {
        return if (children % 2 == 0) {
            adults + children / 2
        } else adults + (children - 1) / 2
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onValueSelected(value: String) {
        currentRoomInformation = Json.decodeFromString(value)

        val adultStr: String = "${currentRoomInformation!!.adult} adult" + if (currentRoomInformation!!.adult > 1) "s" else ""
        val childStr: String = when (currentRoomInformation!!.children) {
            0 -> "No child"
            1 -> "1 child"
            else -> "${currentRoomInformation!!.children} children"
        }


        roomET?.setText("$adultStr - $childStr")
        modalBottomSheet.dismiss()
    }

}