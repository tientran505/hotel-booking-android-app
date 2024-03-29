package com.example.stayfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.hotel.RatingActivity
import com.example.stayfinder.model.HotelDetailModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var activeBtn: android.widget.Button? = null
    var doneBtn: android.widget.Button? = null
    var cancelBtn: android.widget.Button? = null
    var rv: RecyclerView?= null
    var listbooking  : ArrayList<Booking> = arrayListOf()
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    fun messageLayoutShow(mess: String, submess: String, login: Boolean, view: View){
        val messTv = view.findViewById<TextView>(R.id.messTv)
        val submessTv = view.findViewById<TextView>(R.id.submessTv)
//        val loginBtn = view.findViewById<Button>(R.id.loginBtn)

        messTv.setText(mess)
        submessTv.setText(submess)
//        if(login == false ) loginBtn.setVisibility(View.VISIBLE)
//        else loginBtn.setVisibility(View.INVISIBLE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = BookingAdapter(listbooking)
        val auth = FirebaseAuth.getInstance().currentUser

        var view= inflater.inflate(R.layout.fragment_booking, container, false)
        rv = view.findViewById(R.id.rv)
        rv?.layoutManager = LinearLayoutManager(this.context) //GridLayoutManager(this, 2)
        val layout = view.findViewById<ConstraintLayout>(R.id.messageLayout)
        rv?.adapter = adapter
        println(auth?.uid)
        activeBtn = view.findViewById(R.id.activeBtn)
        doneBtn = view.findViewById(R.id.doneBtn)
        cancelBtn = view.findViewById(R.id.cancelBtn)
        progressBar = view!!.findViewById<ProgressBar>(R.id.savedListPB)
        if(auth != null ) {
            FirebaseFirestore.getInstance().collection("bookings")
                .whereEqualTo("user_id", auth.uid).limit(6)
                .get()
                .addOnSuccessListener { documents ->
                    if(documents.size()==0) {
                        layout.setVisibility(View.VISIBLE)
                        messageLayoutShow("Where to next?","You haven't started any trips yet. Once you make a booking, it'll appear here",true, layout)
                        rv?.setVisibility(View.INVISIBLE)
                    }
                    else{
                        layout.setVisibility(View.INVISIBLE)
                        rv?.setVisibility(View.VISIBLE)
                    }
                    for (document in documents) {
                        println("1")
                        val bookingDb = document.toObject(BookingDetail::class.java)
                        listbooking.add(
                            Booking(
                                bookingDb.hotel?.id!!,
                                bookingDb.hotel?.hotel_name!!,
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format((bookingDb.date_start?.toDate())),
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format((bookingDb.date_end?.toDate())),
                                bookingDb.total_price,
                                bookingDb.status,
                                URL(bookingDb.hotel!!.photoUrl[0])
                            )
                        )
//                        adapter.notifyItemChanged(listbooking.size-1)
                    }
                    var ListDefault: ArrayList<Booking> = listbooking.filter { it.status == "Active" } as ArrayList<Booking>
                    adapter.updateList(ListDefault)
                    progressBar.visibility = View.GONE
                }
        }
        else{
            progressBar.visibility = View.GONE
            layout.setVisibility(View.VISIBLE)
            messageLayoutShow("Login or Register to Start","",false, layout)
            rv?.setVisibility(View.INVISIBLE)
        }


        activeBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
        doneBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
        doneBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
        activeBtn!!.setOnClickListener(){
            activeBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
            doneBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            cancelBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            var activeList: ArrayList<Booking> = listbooking.filter { it.status == "Active" } as ArrayList<Booking>

            adapter.updateList(activeList)
            if(auth != null && activeList.size != 0 ){
                layout.setVisibility(View.INVISIBLE)
                rv?.setVisibility(View.VISIBLE)
            }
            else if(auth != null){
                layout.setVisibility(View.VISIBLE)
                messageLayoutShow("Where to next?","You haven't started any trips yet. Once you finish a booking, it'll appear here",true, layout)
                rv?.setVisibility(View.INVISIBLE)
            }
            else{
                layout.setVisibility(View.VISIBLE)
                messageLayoutShow("Login or Register to Start","",false, layout)
                rv?.setVisibility(View.INVISIBLE)
            }

        }
        doneBtn!!.setOnClickListener(){

            doneBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
            activeBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            cancelBtn!!.setBackgroundResource(R.drawable.custom_button_booking)

            var DoneList: ArrayList<Booking> = listbooking.filter { it.status == "Completed" } as ArrayList<Booking>
            adapter.updateList(DoneList)
            if(auth != null && DoneList.size != 0 ){
                layout.setVisibility(View.INVISIBLE)
                rv?.setVisibility(View.VISIBLE)
            }
            else if(auth != null){
                layout.setVisibility(View.VISIBLE)
                messageLayoutShow("There was no booking Done","You haven't make any trip. Let's start one",true, layout)
                rv?.setVisibility(View.INVISIBLE)
            }
            else{
                layout.setVisibility(View.VISIBLE)
                messageLayoutShow("Login or Register to Start","",false, layout)
                rv?.setVisibility(View.INVISIBLE)
            }

            adapter.onItemClick={position->
                val intent = Intent(activity, RatingActivity::class.java)
                intent.putExtra("hotel_id",DoneList[position].id)
                startActivity(intent)
            }
        }
        cancelBtn!!.setOnClickListener(){
            cancelBtn!!.setBackgroundResource(R.drawable.custom_button_booking_select)
            doneBtn!!.setBackgroundResource(R.drawable.custom_button_booking)
            activeBtn!!.setBackgroundResource(R.drawable.custom_button_booking)

            var CancelList: ArrayList<Booking> = listbooking.filter { it.status == "Cancel" } as ArrayList<Booking>
            adapter.updateList(CancelList)
            if(auth != null && CancelList.size != 0 ){
                layout.setVisibility(View.INVISIBLE)
                rv?.setVisibility(View.VISIBLE)
            }
            else if(auth != null){
                layout.setVisibility(View.VISIBLE)
                messageLayoutShow("There's nothing to show","Great!!",true, layout)
                rv?.setVisibility(View.INVISIBLE)
            }
            else{
                layout.setVisibility(View.VISIBLE)
                messageLayoutShow("Login or Register to Start","",false, layout)
                rv?.setVisibility(View.INVISIBLE)
            }
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
         * @return A new instance of fragment BookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}