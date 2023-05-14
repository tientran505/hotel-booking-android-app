package com.example.stayfinder.partner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.*
import com.example.stayfinder.hotel.hotel_detail.HotelDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
data class userRealtime(
    val userid: String?,
    val withUser: ArrayList<withuserRealtime>?
)
data class withuserRealtime(
    val userid: String?,
    val message: ArrayList<Message>,
)
class PartnerMessageFragment : Fragment() {
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
    lateinit var recyclerView : RecyclerView
    var UserList: ArrayList<UserMessage> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.partner_fragment_message, container, false)
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser!!
        UserList.add(UserMessage( user!!.displayName,user!!.photoUrl.toString(),user.uid))
        recyclerView= view!!.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter  =  ShowListChat(UserList)
        adapter.onItemClick = {position ->
            val intent = Intent(this.context, ChatActivity::class.java)
            intent.putExtra("user", UserList[position]);
            startActivity(intent)
        }
        recyclerView.adapter= adapter
        println("uid"+user!!.uid)
//        var currentUser: UserMessage = UserMessage("Cien", "","1")
//        val userAT = UserMessage("AT", "", "2")
        val messagecheck1 = CheckinMessage("TiOWbclrs5gahZAKg1MvNVouDKE3","MlHDg6tlG2hXIWbmQTpQfxdW9Cx1",Date(), cimessage("12:00 - 13:00","hi"))
        val messagecheck2 = NormalMessage("MlHDg6tlG2hXIWbmQTpQfxdW9Cx1","TiOWbclrs5gahZAKg1MvNVouDKE3",Date(),"hello")
        val userRealtime =
            arrayListOf(withuserRealtime("MlHDg6tlG2hXIWbmQTpQfxdW9Cx1", arrayListOf<Message>(messagecheck1,messagecheck2)))

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message").child("TiOWbclrs5gahZAKg1MvNVouDKE3")
        myRef.setValue(userRealtime)
        val myRef2 = database.getReference("message").child("MlHDg6tlG2hXIWbmQTpQfxdW9Cx1")
        val userRealtime2 = arrayListOf(withuserRealtime("TiOWbclrs5gahZAKg1MvNVouDKE3", arrayListOf<Message>(messagecheck1,messagecheck2)))
        myRef2.setValue(userRealtime2)
//        adapter.onItemClick ={
//                position ->
//            run {
//                val intent = Intent(this, EditImagePage::class.java)
//                intent.putExtra("id", IdList[position])
//                if (position == 0)
//                    intent.putExtra("collection", "Hotels")
//                else
//                    intent.putExtra("collection", "rooms")
//
//                startActivity(intent)
//            }
//        }

        return view
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerMessageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerMessageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
class ShowListChat(private var item: ArrayList<UserMessage>) : RecyclerView.Adapter<ShowListChat.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val photoImg = listItemView.findViewById<ImageView>(R.id.photoImg)
        val nameTv = listItemView.findViewById<TextView>(R.id.nameTv)
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowListChat.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.show_list_chat_adapter, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ShowListChat.ViewHolder, position: Int) {
//        println("length "+ item.size)
        println(this.item[position])
        holder.nameTv?.setText(this.item[position].displayName)
        Glide.with(holder.itemView)
            .load(URL(this.item[position].photoUrl))
            .apply(RequestOptions().centerCrop())
            .into(holder.photoImg)
    }
    override fun getItemCount(): Int {
        return item.size
    }

    fun updateList( list: ArrayList<UserMessage>){
        this.item = list
        this.notifyDataSetChanged()
    }
}