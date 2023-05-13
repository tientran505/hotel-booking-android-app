package com.example.stayfinder.services.room

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

class RoomAddHotelStep4Activity : AppCompatActivity() {
    private lateinit var facilitiesLL: LinearLayout
    private lateinit var continueBtn: Button

    private val checkedItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_add_hotel_step4)

        facilitiesLL = findViewById(R.id.facilitiesLL)
        continueBtn = findViewById(R.id.continueBtn)

        val room = intent.getSerializableExtra("roomInfo") as RoomDetailModel?

        continueBtn.setOnClickListener {
            val intent = Intent(this, RoomAddHotelDetailConfirmActivity::class.java)
            room?.apply {
                facilities.clear()
                facilities.addAll(checkedItems)
            }

            Log.d("current_room", room.toString())

            intent.putExtra("roomInfo", room)
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