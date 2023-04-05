package com.example.stayfinder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.city.City
import com.example.stayfinder.city.CityAdapter
import com.example.stayfinder.hotel.Hotel
import com.example.stayfinder.hotel.HotelAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.Timestamp
import java.util.*
import kotlin.math.log
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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


    private lateinit var rvCity: RecyclerView
    private lateinit var cityAdapter: CityAdapter

    private lateinit var rvHotel: RecyclerView
    private lateinit var hotelAdapter: HotelAdapter

    private lateinit var listener: RoomSelectionBottomSheetDialog.BottomSheetListener
    private lateinit var modalBottomSheet: RoomSelectionBottomSheetDialog

    private var currentRoomInformation: RoomInformation? = null

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initRoomSelection(view)
        initCityName(view)
        initCuratedHotel(view)

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
                Toast.makeText(requireContext(), "${dateRangePicker.headerText} is selected"
                    , Toast.LENGTH_LONG).show()
                dateET?.setText(dateRangePicker.headerText)
            }

            dateRangePicker.addOnNegativeButtonClickListener {
                Toast.makeText(requireContext(), "${dateRangePicker.headerText} is cancelled"
                    , Toast.LENGTH_LONG).show()
            }

            dateRangePicker.addOnCancelListener {
                Toast.makeText(requireContext(), "DateRangePicker is cancelled"
                    , Toast.LENGTH_LONG).show()
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
    }

    private fun initCityName(view: View) {
        val cities = listOf(
            City("HCMC"),
            City("Hanoi"),
            City("Da Lat"),
            City("Da Nang"),
            City("Vung Tau"),
            City("Can Tho"),
            City("Phu Quoc"),
            City("Sa Pa"),
            City("Ha Giang"),
            City("Bac Ninh"),
            City("My Tho")
        )

        rvCity = view.findViewById(R.id.cityNameRV)
        rvCity.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        cityAdapter = CityAdapter(cities)
        cityAdapter.onItemClick = { position ->
            Log.i("ttlog", position.toString())
            cityAdapter.setSelectedPosition(position)
        }
        rvCity.adapter = cityAdapter
    }

    private fun initCuratedHotel(view: View) {
        val hotels = listOf(
            Hotel("HCMC", "Sherwood Residence", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("HCMC", "Somerset Ho Chi Minh City", 5.0.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("HCMC", "SILA Urban Living", 3.3.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("HCMC", "La vela Saigon Hotel", 2.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("HCMC", "Novotel Saigon", 3.0.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("HCMC", "Villa Song Saigon", 4.8.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("HCMC", "Norfolk mansion - Luxury Service Ap..", 50.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("HCMC", "CityHouse - Ariosa", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
            Hotel("HCMC", "Sherwood Residence", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), true),
            Hotel("HCMC", "Sherwood Residence", 4.5.toFloat(), 4500000.0.toFloat(),
                3200000.0.toFloat(), false),
        )

        rvHotel = view.findViewById(R.id.curatedHotelRV)
        rvHotel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        hotelAdapter = HotelAdapter(hotels)
        hotelAdapter.onImageViewClick = {position ->
            Log.i("hotel", "Bookmark $position clicked")
            hotelAdapter.setBookmark(position)
        }

        hotelAdapter.onItemClick = {position ->
            Log.i("hotel", "Item $position clicked")
        }

        rvHotel.adapter = hotelAdapter

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

        val roomStr: String = "${currentRoomInformation!!.room} room" + if (currentRoomInformation!!.room > 1) "s" else ""
        val adultStr: String = "${currentRoomInformation!!.adult} adult" + if (currentRoomInformation!!.adult > 1) "s" else ""
        val childStr: String = when (currentRoomInformation!!.children) {
            0 -> "No child"
            1 -> "1 child"
            else -> "${currentRoomInformation!!.children} children"
        }


        roomET?.setText("$roomStr - $adultStr - $childStr")
        modalBottomSheet.dismiss()
    }

}