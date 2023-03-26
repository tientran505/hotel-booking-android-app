package com.example.stayfinder

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BottomSheetListener {
    fun onBottomSheetDismissed(text: String)
}

class RoomSelectionBottomSheetDialog(text: String) : BottomSheetDialogFragment() {
    var roomLabel: TextView? = null
    var roomAdd: Button? = null
    var roomRemove: Button? = null

    var adultLabel: TextView? = null
    var adultAdd: Button? = null
    var adultRemove: Button? = null

    var childLabel: TextView? = null
    var childAdd: Button? = null
    var childRemove: Button? = null

    val MIN_ROOM = 1
    val MAX_ROOM = 10
    val MIN_ADULTS = 1
    val MAX_ADULTS = 25
    val MIN_CHILD = 0
    val MAX_CHILD = 10

    private var mListener: BottomSheetListener? = null

    fun setListener(listener: BottomSheetListener) {
        mListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.room_information_bottom_sheet, container, false)

        roomLabel = view.findViewById(R.id.roomLabel)
        roomAdd = view.findViewById(R.id.room_add_btn)
        roomRemove = view.findViewById(R.id.room_minus_btn)

        adultLabel = view.findViewById(R.id.adultLabel)
        adultAdd = view.findViewById(R.id.adult_add_btn)
        adultRemove = view.findViewById(R.id.adult_minus_btn)

        childLabel = view.findViewById(R.id.childLabel)
        childAdd = view.findViewById(R.id.child_add_btn)
        childRemove = view.findViewById(R.id.child_minus_btn)

        var currentRoom = roomLabel?.text.toString().toInt()
        roomAdd?.isEnabled = currentRoom <= MAX_ROOM
        roomRemove?.isEnabled = currentRoom > MIN_ROOM


        var currentAdult = adultLabel?.text.toString().toInt()
        adultAdd?.isEnabled = currentAdult <= MAX_ADULTS
        adultRemove?.isEnabled = currentAdult > MIN_ADULTS

        var currentChild = childLabel?.text.toString().toInt()
        childAdd?.isEnabled = currentChild <= MAX_CHILD
        childRemove?.isEnabled = currentChild > MIN_ADULTS

        roomAdd?.setOnClickListener {
            currentRoom = roomLabel?.text.toString().toInt() + 1

            roomAdd?.isEnabled = currentRoom < MAX_ROOM
            roomLabel?.text = currentRoom.toString()
            roomRemove?.isEnabled = true
        }

        roomRemove?.setOnClickListener {
            currentRoom = roomLabel?.text.toString().toInt() - 1

            roomRemove?.isEnabled = currentRoom > MIN_ROOM
            roomLabel?.text = currentRoom.toString()
            roomAdd?.isEnabled = true
        }

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
            dismiss()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as BottomSheetListener
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        mListener?.onBottomSheetDismissed("Result")
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}