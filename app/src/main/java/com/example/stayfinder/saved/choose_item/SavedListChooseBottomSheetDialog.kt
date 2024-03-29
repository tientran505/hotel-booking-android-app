package com.example.stayfinder.saved.choose_item

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R
import com.example.stayfinder.coupon
import com.example.stayfinder.hotels
import com.example.stayfinder.saved.model.SavedHotelItem
import com.example.stayfinder.saved_list_item
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedListChooseBottomSheetDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedListChooseBottomSheetDialog(hotel_id:String, hotel_name:String, hotel_img:String) : BottomSheetDialogFragment(), CoroutineScope by MainScope() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var hotel_id = hotel_id
    var hotel_name = hotel_name
    var hotel_img = hotel_img

    private lateinit var collectionRV: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var collectionAdapter: HotelCollectionAdapter
    private var collectionList = ArrayList<list>()

    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore

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
        val view = inflater.inflate(
            R.layout.fragment_saved_list_choose_bottom_sheet_dialog,
            container,
            false
        )

        initComponent(view)
        configRecyclerView()
        fetchData()

        return view
    }

    private fun initComponent(view: View) {
        collectionRV = view.findViewById(R.id.collectionRV)
        progressBar = view.findViewById(R.id.progressBarCollection)

    }

    private fun configRecyclerView() {
        collectionRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL
            , false)

        collectionAdapter = HotelCollectionAdapter(requireContext(), collectionList)

        collectionAdapter.onButtonClick = { position ->
            //            val vh = hotelSearchRV.findViewHolderForAdapterPosition(position) as HotelSearchAdapter.ViewHolder
            val vh = collectionRV.findViewHolderForAdapterPosition(position) as HotelCollectionAdapter.ViewHolder
            Log.d("test",collectionList[position].isAdded.toString())
            if (collectionList[position].isAdded) {
                collectionList[position].isAdded = false
                collectionAdapter.notifyItemChanged(position)
                removeHotelFromSavedList(collectionList[position].doc_id, position, collectionList[position].name_list)
                collectionList[position].doc_id = ""
            }
            else {
                collectionList[position].isAdded = true
                collectionAdapter.notifyItemChanged(position)
                addHotelToSavedList(position,collectionList[position].id,collectionList[position].name_list)
            }

            //collectionList[position].isAdded = !collectionList[position].isAdded
        }

        collectionRV.adapter = collectionAdapter
    }

    private fun addHotelToSavedList(pos: Int, collection_id: String, collectionName: String) {
        val id = db.collection("saved_list_items").document().id
        collectionList[pos].doc_id = id
        collectionList[pos].number_of_item += 1
        var chosen_item = saved_list_item(id, hotel_id, collection_id, hotel_name, hotel_img)
        //val item = SavedHotelItem(id, user_id, collection_id)

        val docRef = db.collection("saved_list_items")
            .document(id)

        docRef
            .set(chosen_item)
            .addOnSuccessListener {
                Toast.makeText(this@SavedListChooseBottomSheetDialog.requireContext(), "Successfully added to $collectionName"
                    , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@SavedListChooseBottomSheetDialog.requireContext(), "Fail to add item into $collectionName"
                    , Toast.LENGTH_SHORT).show()
            }

        db.collection("saved_lists").document(collectionList[pos].id).update("number_of_item", collectionList[pos].number_of_item)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    private fun removeHotelFromSavedList(document_id: String, pos: Int, collectionName: String) {
        Log.d("rad", "doc_id: $document_id ")
        db.collection("saved_list_items")
            .document(document_id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this@SavedListChooseBottomSheetDialog.requireContext(), "Successfully removed from $collectionName"
                    , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@SavedListChooseBottomSheetDialog.requireContext(), "Fail to remove item from $collectionName"
                    , Toast.LENGTH_SHORT).show()
            }

        collectionList[pos].number_of_item -= 1
        db.collection("saved_lists").document(collectionList[pos].id).update("number_of_item", collectionList[pos].number_of_item)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    private fun fetchData() {
        if (user != null) {
            val documents = db.collection("saved_lists")
                .whereEqualTo("user_id", user.uid)
                .orderBy("create_date", Query.Direction.DESCENDING)

            documents.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("tienlog", "DocumentSnapshot data: ${document.size()}")
                    } else {
                        Log.d("tienlog", "No such document")
                    }

                    val datas = document.documents

                    for (data in datas) {
                        val mylist = data.toObject(list::class.java) as list
                        Log.d("skt", "Data: $mylist")
                        collectionList.add(mylist)
                        checkHotelInList(mylist.id, collectionList.size - 1)
                        collectionAdapter.notifyItemInserted(collectionList.size - 1)
                    }

                    progressBar.visibility = View.GONE
                }
        }
    }

    private fun checkHotelInList(list_id:String, pos: Int){
        val documents = db.collection("saved_list_items")
            .whereEqualTo("list_id", list_id)
            .whereEqualTo("hotel_id",hotel_id)
            .get()
            .addOnSuccessListener {documents ->
                for(document in documents){
                    val result= document.toObject(saved_list_item::class.java)
                    collectionList[pos].doc_id=result.id
                    if(collectionList[pos].doc_id != ""){
                        collectionList[pos].isAdded=true
                        collectionAdapter.notifyItemChanged(pos)
                    }
                }
            }

    }

    companion object {
        const val TAG = "ChooseItemListFragment"
    }
}