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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var dateET: EditText? = null
    private var roomET: EditText? = null

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
            val modalBottomSheet = RoomSelectionBottomSheetDialog("")


            modalBottomSheet.show(requireActivity().supportFragmentManager
                , RoomSelectionBottomSheetDialog.TAG)
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
}