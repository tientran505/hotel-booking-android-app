package com.example.stayfinder.saved.choose_item

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        collectionRV = view.findViewById(R.id.collectionRV)
        collectionRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL
            , false)

        progressBar = view.findViewById(R.id.progressBarCollection)
        collectionAdapter = HotelCollectionAdapter(requireContext(), collectionList)
        collectionRV.adapter = collectionAdapter

        launch {
            fetchData()
        }
        return view
    }

    private suspend fun fetchData() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val db = Firebase.firestore
            val documents = db.collection("save_lists")
                .whereEqualTo("user_id", user.uid)
                .get()
                .await()
            for (document in documents) {
                val item = document.toObject<Hotel>()
                collectionList.add(item)
                Log.i("itemLog", item.toString())
                collectionAdapter.notifyItemInserted(collectionList.size - 1)
            }
        }

        progressBar.visibility = View.GONE
    }

    companion object {
        const val TAG = "ChooseItemListFragment"
    }
}