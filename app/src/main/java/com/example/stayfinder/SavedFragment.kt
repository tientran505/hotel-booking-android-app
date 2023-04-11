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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var itemList = arrayListOf<SavedListItem>()
    var savedList = arrayListOf<SavedList>()
    val db = Firebase.firestore

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

        var view: View? = null
        view = inflater.inflate(R.layout.fragment_saved, container, false)

        /*val test = db.collection("saved_lists").
        document("Q2gClMdVTmztDQjjwZeP").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("test", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("test", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("test","get failed with ", exception)
            }*/

        /*val testHotel = Hotel("The Sóng Vũng Tàu Homestay - Vũng Tàu Land","28 Thi Sách, " +
                "Khách sạn Signature Boutique Hotel hiện đại, " +
                "trang nhã này có các phòng kiểu boutique với Wi-Fi miễn phí và nhà hàng riêng. " +
                "Khách sạn tọa lạc tại một con hẻm yên tĩnh, cách Đường Nguyễn Trãi ở " +
                "Thành phố Hồ Chí Minh chỉ vài bước chân.  Signature Boutique Hotel cách chợ Bến Thành 2 km. " +
                "Các điểm tham quan địa phương bao gồm Bảo tàng Chiến tranh, Nhà thờ Đức Bà và Dinh Thống Nhất")
        Log.d("test",testHotel.id!! + " " + testHotel.name!!)
        db.collection("Hotel").document(testHotel.id!!).set(testHotel)
            .addOnSuccessListener { Log.d("test", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("test", "Error writing document", e) }*/

        val docRef = db.collection("Hotel").document("1998e0ba-b233-49f9-bbaa-eb5abccf043b")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val hotel = documentSnapshot.toObject(Hotel::class.java)
            Log.d("test", hotel.toString())
        }

        itemList.add(SavedListItem("Căn nhà mơ ước", "Vũng Tàu", R.drawable.purpl))
        itemList.add(SavedListItem("Homestay trong mơ", "Vũng Tàu", R.drawable.purpl))
        itemList.add(SavedListItem("Rất tuyệt vời", "Vũng Tàu", R.drawable.purpl))

        savedList.add(SavedList("Vũng Tàu", "5 saved"))
        savedList.add(SavedList("Vũng Tàu", "5 saved"))

        val dialog = this.context?.let { BottomSheetDialog(it) }
        val viewdia = layoutInflater.inflate(R.layout.list_dialog_layout, null)
        dialog!!.setContentView(viewdia)

        val addDialog = Dialog(this.requireContext())
        addDialog.setContentView(R.layout.custom_addlist_dialog)

        val myList = view.findViewById<RecyclerView>(R.id.horizontalScrollView) as RecyclerView

        var horadapter = HorizontalAdapter(itemList)
        myList.adapter = horadapter

        myList.layoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.HORIZONTAL, false)


        horadapter.setOnItemClickListener(object: HorizontalAdapter.onItemClickListener{
            override fun onItemClick(position: Int){

            }
        })

        val allList = view.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        var listadapter = SavedListAdapter(savedList)
        allList.adapter = listadapter
        allList.layoutManager = LinearLayoutManager(this.context)

        listadapter.setOnIconClickListener((object : SavedListAdapter.onIconClickListener{
            override fun onIconClick(position: Int) {
                dialog.show()
            }
        }))

        listadapter.setOnItemClickListener((object : SavedListAdapter.onItemClickListener{
            override fun onItemClick(position: Int){
                val intent = Intent(activity, DetailListActivity::class.java)
                startActivity(intent)
            }
        }))

        val addbtn = view.findViewById<Button>(R.id.button)
        addbtn.setOnClickListener{
            addDialog.show()
            val lname = addDialog.findViewById<EditText>(R.id.addListName)
            addbtn.setText(lname.text)
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