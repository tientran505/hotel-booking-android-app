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
        dateStart.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker(dateStart)
//                view.hideKeyboard()
            }
        }
        dateStart.setOnClickListener{
            showDatePicker(dateStart)
        }
        dateEnd.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker(dateEnd)
//                view.hideKeyboard()
            }
        }
        dateEnd.setOnClickListener{
            showDatePicker(dateEnd)
        }
        return view
    }
    private fun showDatePicker(datetv: TextView) {
        val datestring = datetv.text.toString()
        val day : Int = datestring.substringBefore('/',datestring).toInt()
        val month : Int = datestring.substringAfter('/',datestring).substringBefore('/',datestring).toInt() -1
        val year : Int = datestring.substringAfter('/',datestring).substringAfter('/',datestring).toInt()

        val datePickerDialog = DatePickerDialog(this.requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val validDate = "0${dayOfMonth}".takeLast(2) + "/" +
                    "0${monthOfYear + 1}".takeLast(2) + "/" + "$year"

            datetv.text = validDate

            if(!checkDateValidate(dateStart.text.toString(), dateEnd.text.toString())){

                if(datetv.id == dateStart.id){
                    Toast.makeText(this.context, "You have choose wrong date, start date is after end date, please choose another start date", Toast.LENGTH_LONG).show()
                    showDatePicker(dateStart)
                }
                else{
                    Toast.makeText(this.context, "You have choose wrong date, end date is before start date, please choose another end date", Toast.LENGTH_LONG).show()
                    showDatePicker(dateEnd)
                }
            }
        }, year, month, day)
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog!!.show()
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
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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