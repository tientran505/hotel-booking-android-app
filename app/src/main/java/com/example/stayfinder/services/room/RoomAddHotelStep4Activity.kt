package com.example.stayfinder.services.room

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginTop
import com.example.stayfinder.R
import com.example.stayfinder.model.RoomDetailModel
import com.example.stayfinder.partner.room.PartnerListRoomActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RoomAddHotelStep4Activity : AppCompatActivity() {
    private lateinit var facilitiesLL: LinearLayout
    private lateinit var continueBtn: Button
    private var progressDialog: ProgressDialog? = null


    private val checkedItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_step4)

        facilitiesLL = findViewById(R.id.facilitiesLL)
        continueBtn = findViewById(R.id.finishBtn)

        progressDialog = ProgressDialog(this)

        val room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?
        val timestamp = intent.getLongExtra("timestamp", System.currentTimeMillis())

        continueBtn.setOnClickListener {
            room?.apply {
                facilities.clear()
                facilities.addAll(checkedItems)
                created_date = Timestamp(Date(System.currentTimeMillis()))
                available_start_date = Timestamp(Date(timestamp))
            }

            val db = Firebase.firestore

            progressDialog?.setTitle("Please wait")
            progressDialog?.setMessage("Submitting...")
            progressDialog?.show()

            db.collection("rooms")
                .document(room!!.id)
                .set(room)
                .addOnSuccessListener {
                    val intent = Intent(this, PartnerListRoomActivity::class.java)
                    intent.putExtra("uuidHotel", room.hotel_id)

                    progressDialog?.dismiss()
                    Toast.makeText(this, "Add new room successfully", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    progressDialog?.dismiss()
                    Toast.makeText(this, "Error adding document: $e", Toast.LENGTH_SHORT).show()
                }


            Log.d("current_room", room.toString())

//            intent.putExtra("roomInfo", room)
        }

        initActionBar()
        initCheckBoxes()
    }

    private fun initCheckBoxes() {
        val items = resources.getStringArray(R.array.facilities_item)

        for (item in items) {
            val checkBox = CheckBox(this)
            checkBox.text = item
            checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    checkedItems.add(compoundButton.text.toString())
                }
                else {
                    checkedItems.remove(compoundButton.text.toString())
                }
            }

            facilitiesLL.addView(checkBox)

            val divider = DashedDivider(this)
            facilitiesLL.addView(divider)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Facilities"
    }

    class DashedDivider(context: Context) : View(context) {

        private val paint = Paint()

        init {
            val density = context.resources.displayMetrics.density
            val margin = (15 * density).toInt()
            val dashLength = (10 * density).toInt()
            val dashGap = (10 * density).toInt()

            paint.color = Color.GRAY
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2 * density
            paint.pathEffect = DashPathEffect(floatArrayOf(dashLength.toFloat(), dashGap.toFloat()), 0f)

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2
            ).apply {
                setMargins(margin , margin, 0, margin)
            }
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, paint)
        }
    }
}