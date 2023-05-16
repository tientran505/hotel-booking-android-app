package com.example.stayfinder

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class RoomInformation(val adult: Int, val children: Int)

class RoomSelectionBottomSheetDialog(val content: String, listener: BottomSheetListener) : BottomSheetDialogFragment() {
    var adultLabel: TextView? = null
    var adultAdd: Button? = null
    var adultRemove: Button? = null

    var childLabel: TextView? = null
    var childAdd: Button? = null
    var childRemove: Button? = null

    val MIN_ADULTS = 1
    val MAX_ADULTS = 25
    val MIN_CHILD = 0
    val MAX_CHILD = 10

    private var mListener: BottomSheetListener? = null
    init {
        this.mListener = listener
    }

    interface BottomSheetListener {
        fun onValueSelected(value: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.room_information_bottom_sheet, container, false)

        adultLabel = view.findViewById(R.id.adultLabel)
        adultAdd = view.findViewById(R.id.adult_add_btn)
        adultRemove = view.findViewById(R.id.adult_minus_btn)

        childLabel = view.findViewById(R.id.childLabel)
        childAdd = view.findViewById(R.id.child_add_btn)
        childRemove = view.findViewById(R.id.child_minus_btn)

        if (this.content != "") {
            val room: RoomInformation = Json.decodeFromString(content)
            adultLabel?.text = room.adult.toString()
            childLabel?.text = room.children.toString()
        }


        var currentAdult = adultLabel?.text.toString().toInt()
        adultAdd?.isEnabled = currentAdult <= MAX_ADULTS
        adultRemove?.isEnabled = currentAdult > MIN_ADULTS

        var currentChild = childLabel?.text.toString().toInt()
        childAdd?.isEnabled = currentChild <= MAX_CHILD
        childRemove?.isEnabled = currentChild > MIN_ADULTS

        adultAdd?.setOnClickListener {
            currentAdult = adultLabel?.text.toString().toInt() + 1

            adultAdd?.isEnabled = currentAdult < MAX_ADULTS
            adultLabel?.text = currentAdult.toString()
            adultRemove?.isEnabled = true
        }

        adultRemove?.setOnClickListener {
            currentAdult = adultLabel?.text.toString().toInt() - 1

            adultRemove?.isEnabled = currentAdult > MIN_ADULTS
            adultLabel?.text = currentAdult.toString()
            adultAdd?.isEnabled = true
        }

        childAdd?.setOnClickListener {
            currentChild = childLabel?.text.toString().toInt() + 1

            childAdd?.isEnabled = currentChild < MAX_CHILD
            childLabel?.text = currentChild.toString()
            childRemove?.isEnabled = true
        }

        childRemove?.setOnClickListener {
            currentChild = childLabel?.text.toString().toInt() - 1

            childRemove?.isEnabled = currentChild > MIN_CHILD
            childLabel?.text = currentChild.toString()
            childAdd?.isEnabled = true
        }

        view.findViewById<Button>(R.id.applyBtn).setOnClickListener {
            val value = Json.encodeToString(RoomInformation(adultLabel?.text.toString().toInt(),
                childLabel?.text.toString().toInt()))

            mListener?.onValueSelected(value)
        }

        return view
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}