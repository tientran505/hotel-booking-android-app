package com.example.stayfinder.hotel.hotel_detail

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stayfinder.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubHotelDetailPeriod.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubHotelDetailPeriod : Fragment() {
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
    lateinit var periodTv: TextView
    lateinit var costTv: TextView
    lateinit var noPeople:TextView
    lateinit var noRoom:TextView
    lateinit var noKid: TextView
    var daysDiff: Long = 0
    var price: Double =0.0
    fun openDialog(){

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_sub_hotel_detail_period_cost, container, false)
//        val bookingDetail :HotelDetails ? = this.getArguments()?.getSerializable("BookingDetail") as HotelDetails?

        val dateStartString : String? = this.getArguments()?.getString("dateStart")
        val dateEndString: String? =this.getArguments()?.getString("dateEnd")
        val detailRoom: ArrayList<Int>? =this.getArguments()?.getIntegerArrayList("detailRoom")
//        price = bookingDetail!!.priceless!!
        dateStart = view!!.findViewById(R.id.datestartTv)
        dateEnd = view.findViewById(R.id.dateendTv)
        periodTv = view.findViewById(R.id.periodTv)
        costTv = view.findViewById(R.id.costTv)
        noRoom = view.findViewById(R.id.noRoomTv)
        noPeople = view.findViewById(R.id.noPeopleTv)
        noKid = view.findViewById(R.id.nopKidTv)

        if(detailRoom != null){
            noRoom.setText(detailRoom?.get(0).toString()+" Room(s)")
            noPeople.setText(detailRoom?.get(1).toString()+" People(s)")
            noKid.setText(detailRoom?.get(2).toString()+" Kid(s)")
            if(detailRoom?.get(2)==0)
                noKid.setText("No Kid Include")
        }
        else{
            noRoom.setText("click here to choose room")
        }
        noRoom.setOnClickListener(){
            openDialog()
        }
        noPeople.setOnClickListener(){
            openDialog()
        }
        noKid.setOnClickListener(){
            openDialog()
        }
        dateStart.inputType = InputType.TYPE_NULL
        dateEnd.inputType = InputType.TYPE_NULL
        dateStart.text = dateStartString
        dateEnd.text = dateEndString
        setDay()
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

                val formatter = SimpleDateFormat("dd-MM-yyyy")
                val start = formatter.format(it.first)
                dateStart.setText(start.toString())
//                calendar.timeInMillis = it.second
                val end = formatter.format(it.second)
                dateEnd.setText(end.toString())
                setDay()
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

    fun setDay()  {
        val startdate = dateStart.text.toString()
        val enddate = dateEnd.text.toString()
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val start = formatter.parse(startdate)
        val end = formatter.parse(enddate)
        daysDiff = TimeUnit.DAYS.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS)
        periodTv.setText("Price for $daysDiff night(s) (" + startdate+ " - "+enddate+")")
        val moneyexchange = DecimalFormat("###,###,###,###.##"+" vnđ");
        costTv.setText(moneyexchange.format(price*daysDiff))
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
            SubHotelDetailPeriod().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}