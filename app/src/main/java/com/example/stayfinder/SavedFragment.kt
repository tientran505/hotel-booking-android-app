package com.example.stayfinder

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stayfinder.partner.property.PartnerCouponList
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
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
 * Use the [SavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedFragment : Fragment(), CoroutineScope by MainScope() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var listadapter:SavedListAdapter

    var itemList = arrayListOf<SavedListItem>()
    var savedList = arrayListOf<SavedList>()
    val db = Firebase.firestore
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

        launch {
            loadSavedLists()
        }
    }

    private suspend fun loadSavedLists() {
        val documents = Firebase.firestore.collection("saved_lists")
            .whereEqualTo("user_id", user!!.uid)
            .orderBy("create_date", Query.Direction.DESCENDING)
            .get()
            .await()
        for (document in documents) {
            val l = document.toObject(saved_lists::class.java)
            savedList.add(SavedList(l.name_list,l.number_of_item.toString() + " items saved", l.id,l.number_of_item.toInt()))
            listadapter.notifyItemInserted(savedList.size - 1)
        }
        progressBar.visibility = View.GONE
    }

    private suspend fun renameList(name: String, pos: Int, id: String){
        savedList[pos].titlename=name
        listadapter.notifyItemChanged(pos)
        db.collection("saved_lists").document(id).update("name_list",name)
    }

    private suspend fun deleteList(pos: Int, id: String){
        savedList.removeAt(pos)
        listadapter.notifyItemRemoved(pos)
        db.collection("saved_lists").document(id).delete()
    }

    private suspend fun addList(name:String){
        val id = db.collection("saved_lists").document().id

        val list: saved_lists = saved_lists(id, user!!.uid, name)
        val docRef = db.collection("saved_lists").document(id)

        docRef.set(list)

        savedList.add(SavedList(name,list.number_of_item.toString() + " items saved",id,list.number_of_item))
        listadapter.notifyItemInserted(savedList.size-1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view: View? = null
        view = inflater.inflate(R.layout.fragment_saved, container, false)

        progressBar = view.findViewById(R.id.savedListPB)

        val dialog = this.context?.let { BottomSheetDialog(it) }
        val viewdia = layoutInflater.inflate(R.layout.list_dialog_layout, null)
        dialog!!.setContentView(viewdia)

        val rename: LinearLayout = dialog.findViewById<LinearLayout>(R.id.rename)!!
        val delete: LinearLayout = dialog.findViewById<LinearLayout>(R.id.delete)!!

        val addbutton = view.findViewById<MaterialButton>(R.id.addListBtn)


        val allList = view.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        listadapter = SavedListAdapter(savedList)
        allList.adapter = listadapter
        allList.layoutManager = LinearLayoutManager(this.context)

        listadapter.setOnIconClickListener((object : SavedListAdapter.onIconClickListener{
            override fun onIconClick(position: Int) {
                dialog.show()

                rename.setOnClickListener{
                    val renameDialog = Dialog(requireContext())
                    renameDialog.setContentView(R.layout.dialog_rename_saved_list)
                    renameDialog.show()

                    val renamebtn = renameDialog.findViewById<Button>(R.id.renamebtn)
                    renamebtn.setOnClickListener{
                        val lname = renameDialog.findViewById<EditText>(R.id.newListName).text.toString()
                        launch{
                            renameList(lname,position,savedList[position].id!!)
                        }
                        renameDialog.dismiss()
                    }
                }

                delete.setOnClickListener{
                    launch{
                        deleteList(position,savedList[position].id!!)
                    }
                }
            }
        }))

        listadapter.setOnItemClickListener((object : SavedListAdapter.onItemClickListener{
            override fun onItemClick(position: Int){
                val intent = Intent(activity, DetailListActivity::class.java)
                intent.putExtra("list_id", savedList[position].id)
                intent.putExtra("list_name", savedList[position].titlename)
                intent.putExtra("number_item", savedList[position].n_o_i)
                startActivity(intent)
            }
        }))

        val addDialog = Dialog(this.requireContext())
        addDialog.setContentView(R.layout.dialog_add_saved_list)

        addbutton.setOnClickListener{
            addDialog.show()
            val addListButton = addDialog.findViewById<Button>(R.id.addlistbtn)
            addListButton.setOnClickListener {
                val lname = addDialog.findViewById<EditText>(R.id.addListName).text.toString()
                Log.d("test", lname)
                launch{
                    addList(lname)
                }
                addDialog.dismiss()
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
         * @return A new instance of fragment SavedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}