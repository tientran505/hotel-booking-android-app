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
import com.example.stayfinder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    val message: ArrayList<Message?>,
):java.io.Serializable{
    constructor():this("", arrayListOf()) }
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
//        UserList.add(UserMessage( user!!.displayName,user!!.photoUrl.toString(),user.uid))
        recyclerView= view!!.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter  =  ShowListChat(UserList)
        adapter.onItemClick = {position ->
            val intent = Intent(this.context, ChatActivity::class.java)
            intent.putExtra("user", UserList[position]);
            startActivity(intent)
        }
        recyclerView.adapter= adapter
        val myRef = FirebaseDatabase.getInstance().reference.child("message").child(user?.uid!!)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userSet = mutableSetOf<String>()
                // Loop through each child node under the "message" root
                for (child in dataSnapshot.children) {
                    // Loop through each child node under the current user ID node
//                        val userId = userChat.child("userid").getValue(String::class.java)
                        val userId = child.key
                        // Add the user ID to the set if it's not the origin user ID
                        if (userId != null && userId != user?.uid) {
                            userSet.add(userId)
                            val documents = Firebase.firestore.collection("users")
                                .document(userId)
                            documents.get().addOnSuccessListener { document ->
                                if (document != null) {
                                    UserList.add(UserMessage( document.get("displayName") as String,
                                        document.getString("photoUrl"),
                                        userId))
                                    adapter.updateList(UserList)
                                }
                            }
//                            println(userSet)
                        }

                }

                // Print the set of user IDs
                println(UserList)
                adapter.updateList(UserList)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })



//
//        val userRef: DatabaseReference =  FirebaseDatabase.getInstance().getReference("message").child(user?.uid!!).child("TiOWbclrs5gahZAKg1MvNVouDKE3").push()
//        userRef.setValue( NormalMessage(user?.uid!!,"TiOWbclrs5gahZAKg1MvNVouDKE3",Date(),"1232131231312321"))

//        userRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val userSet = mutableSetOf<String>()
//                // Loop through each child node under the "message" root
//                for (child in dataSnapshot.children) {
//                    // Loop through each child node under the current user ID node
//                    for (userChat in child.children) {
////                        val userId = userChat.child("userid").getValue(String::class.java)
//                        val userId = userChat.key
//                        // Add the user ID to the set if it's not the origin user ID
//                        if (userId != null && userId != user?.uid) {
//                            userSet.add(userId)
//                            val documents = Firebase.firestore.collection("users")
//                                .document(userId)
//                            documents.get().addOnSuccessListener { document ->
//                                if (document != null) {
//                                    UserList.add(UserMessage( document.get("displayName") as String,
//                                        document.getString("photoUrl"),
//                                        userId))
//                                    adapter.updateList(UserList)
//                                }
//                            }
////                            println(userSet)
//                        }
//                    }
//                }
//
//                // Print the set of user IDs
//                println(UserList)
//                adapter.updateList(UserList)
//
//            }


//        userRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val userIDs: ArrayList<String?> = arrayListOf()
//                for (userSnapshot in dataSnapshot.children) {
//                    val userID = userSnapshot.key
//                    userIDs.add(userID)
//                }
////                println(userIDs)
//                println("uid"+userIDs[0])
//
//                // Use the userIDs ArrayList here...
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
////                Log.e("TAG", "Database error: " + databaseError.message)
//            }
//        })
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