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
import com.example.stayfinder.saved.model.SavedHotelItem
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
class SavedListChooseBottomSheetDialog : BottomSheetDialogFragment(), CoroutineScope by MainScope() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var collectionRV: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var collectionAdapter: HotelCollectionAdapter
    private var collectionList = ArrayList<Hotel>()

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

            if (collectionList[position].isAdded) {
                vh.imgBtn.setImageResource(R.drawable.plus_svgrepo_com)
                if (user != null) {
                    addHotelToSavedList(user.uid, collectionList[position].id,
                        collectionList[position].name_list)
                }
            }
            else {
                vh.imgBtn.setImageResource(R.drawable.check_circle_svgrepo_com)
                if (user != null) {
                    removeHotelFromSavedList(collectionList[position].id,
                        collectionList[position].name_list)
                }
            }

            collectionList[position].isAdded = !collectionList[position].isAdded
        }

        collectionRV.adapter = collectionAdapter
    }

    private fun addHotelToSavedList(user_id: String, collection_id: String, collectionName: String) {
        val id = db.collection("saved_list_items").document().id
        val item = SavedHotelItem(id, user_id, collection_id)

        val docRef = db.collection("saved_list_items")
            .document(id)

        docRef
            .set(item)
            .addOnSuccessListener {
                Toast.makeText(this@SavedListChooseBottomSheetDialog.requireContext(), "Successfully added to $collectionName"
                    , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@SavedListChooseBottomSheetDialog.requireContext(), "Fail to add item into $collectionName"
                    , Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeHotelFromSavedList(document_id: String, collectionName: String) {
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
                        val hotelItem = data.toObject(Hotel::class.java) as Hotel
                        Log.d("skt", "Data: $hotelItem")
                        collectionList.add(hotelItem)
                        collectionAdapter.notifyItemInserted(collectionList.size - 1)
                    }

                    progressBar.visibility = View.GONE
                }
        }
    }

    companion object {
        const val TAG = "ChooseItemListFragment"
    }
}