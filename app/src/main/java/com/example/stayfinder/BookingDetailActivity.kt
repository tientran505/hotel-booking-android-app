package com.example.stayfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class BookingDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)
        val fm: FragmentManager = supportFragmentManager
        val a =bookingDetail("The Sóng Vũng Tàu Homestay - Vũng Tàu Land","30-3-2023", "31-3-2023",8.2,"28 Thi Sách, Phường Thắng Tam, Vũng Tàu Việt Nam",
            arrayListOf(R.drawable.ic_love,R.drawable.ic_home,R.drawable.ic_logout,R.drawable.ic_travel,R.drawable.ic_google,R.drawable.ic_love),false,4.8,"The Sóng Vũng Tàu Homestay - Vũng Tàu Land")
        val bundle = Bundle()
        bundle.putSerializable("BookingDetail", a)
        val fragInfo1 = SubBookingDetailImage()
        fragInfo1.setArguments(bundle);
        val fragInfo2 = SubBookingDetailPeriod()
        fragInfo2.setArguments(bundle);
        val fragInfo3 = SubBookingDetailAddress()
        fragInfo3.setArguments(bundle);
        val fragInfo4 = SubBookingDetailDescription()
        fragInfo4.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fame1, fragInfo1).commit();
        fm.beginTransaction().replace(R.id.fame2, fragInfo2).commit();
        fm.beginTransaction().replace(R.id.fame3, fragInfo3).commit();
        fm.beginTransaction().replace(R.id.fame4, fragInfo4).commit();

        //        fm.beginTransaction().replace(R.id.fame1, subBookingDetail_Image(a.titlename!!,a.img)).commit()
//        fm.beginTransaction().replace(R.id.fame2, subBookingDetail_Image(a.titlename!!,a.img)).commit()

    }

}