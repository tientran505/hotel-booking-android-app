package com.example.stayfinder

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubBookingDetailPeriod.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubBookingDetailPeriod : Fragment() {
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
    lateinit var dateStart: TextView
    lateinit var dateEnd: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dateStartInput :String? = this.arguments?.getString("dateStart")
        val dateEndInput :String? = this.arguments?.getString("dateEnd")
        val view: View? = inflater.inflate(R.layout.fragment_sub_booking_detail_period, container, false)
        dateStart = view!!.findViewById(R.id.datestartTv)
        dateEnd = view.findViewById(R.id.dateendTv)
        dateStart.inputType = InputType.TYPE_NULL
        dateEnd.inputType = InputType.TYPE_NULL
        dateStart.text = dateStartInput
        dateEnd.text = dateEndInput
        dateStart?.setOnClickListener {
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


            dateRangePicker.addOnPositiveButtonClickListener {it->
                Toast.makeText(requireContext(), "${dateRangePicker.headerText} is selected"
                    , Toast.LENGTH_LONG).show()
                val dateperiod = dateRangePicker.headerText.toString()
                print(dateperiod)
//                calendar.setTimeInMillis(it.first);
//                calendar.timeInMillis = it.first
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                val start = formatter.format(it.first)
                dateStart.setText(start.toString())
//                calendar.timeInMillis = it.second
                val end = formatter.format(it.second)
                dateEnd.setText(end.toString())
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

        return view
    }

    fun checkDateValidate(startdate: String, enddate: String) :Boolean{
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val firstDate: Date = sdf.parse(startdate)
        val secondDate: Date = sdf.parse(enddate)
        print(firstDate.toString())
        print(secondDate.toString())
        val cmp = firstDate.compareTo(secondDate)
        when {
            cmp < 0 -> {
                return true
            }
            else -> {
               return false
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubBookingDetailPeriod.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubBookingDetailPeriod().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}