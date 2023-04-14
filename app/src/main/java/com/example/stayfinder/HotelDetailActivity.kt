package com.example.stayfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import java.io.Serializable
import java.net.URL
data class HotelDetails(
    var id: String,
    var hotel_name: String,
    var priceless: Double,
    var img: ArrayList<URL>,
    var rating_overall: Double,
    var address: address,
    var description: String,
    var noFeedback: Int,
    val booking_count: Int,
    var facilities: ArrayList<facilities>,
    val comment_count: Int):Serializable{
    constructor(id: String,hotel_name: String,pricebernight: Double, address: address,img: ArrayList<URL>,rating_overall: Double,description: String):
            this(id,hotel_name,pricebernight,img,rating_overall,address,description,0,0,ArrayList<facilities>(),0)
}
class HotelDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)
        val fm: FragmentManager = supportFragmentManager
        val booking_id = ""
        val dateStart ="30-3-2023"
        val dateEnd = "1-4-2023"
        val a =HotelDetails("1233","The Sóng Vũng Tàu Homestay - Vũng Tàu Land",8.2,address(28,"Thi Sách","Thắng Tam", "","Vũng Tàu"),
            arrayListOf(URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"),URL("https://majestichotelgroup.com/web/majestic/homepage/slider_principal/00-hotel-majestic-barcelona.jpg"))
            ,4.8,
            "Khách sạn Signature Boutique Hotel hiện đại, trang nhã này có các phòng kiểu boutique với Wi-Fi miễn phí và nhà hàng riêng. Khách sạn tọa lạc tại một con hẻm yên tĩnh, cách Đường Nguyễn Trãi ở Thành phố Hồ Chí Minh chỉ vài bước chân.\n" +
                    "\n" +
                    "Signature Boutique Hotel cách chợ Bến Thành 2 km. Các điểm tham quan địa phương bao gồm Bảo tàng Chiến tranh, Nhà thờ Đức Bà và Dinh Thống Nhất cách đó 15 phút lái xe. Từ nơi nghỉ này đến Sân bay Quốc tế Tân Sơn Nhất mất 30 phút đi xe hơi.\n" +
                    "\n" +
                    "Các phòng máy lạnh tại đây có sàn trải thảm và truyền hình cáp màn hình phẳng với truy cập Internet. Các phòng tắm đá cẩm thạch có vách kính trong suốt và đi kèm với vòi sen nước nóng cũng như đồ vệ sinh cá nhân miễn phí.\n" +
                    "\n" +
                    "Ngoài lễ tân 24 giờ, khách sạn còn có bàn bán tour, dịch vụ thu đổi ngoại tệ cũng như dịch vụ đặt vé. Dịch vụ giặt là và ủi được cung cấp với một khoản phụ phí.\n" +
                    "\n" +
                    "Ẩm thực Việt Nam và quốc tế được phục vụ tại nhà hàng trong nhà của khách sạn. Nhà hàng mở cửa từ 07:00-23:00.\n" +
                    "\n" +
                    "Các cặp đôi đặc biệt thích địa điểm này — họ cho điểm 8,4 cho kỳ nghỉ dành cho 2 người.")
        val bundle = Bundle()
        bundle.putSerializable("BookingDetail", a)
        bundle.putString("dateStart", dateStart)
        bundle.putString("dateEnd", dateEnd)
        val fragInfo1 = SubHotelDetailImage()
        fragInfo1.setArguments(bundle);
        val fragInfo2 = SubHotelDetailPeriod()
        fragInfo2.setArguments(bundle);
        val fragInfo3 = SubHotelDetailAddress()
        fragInfo3.setArguments(bundle);
        val fragInfo4 = SubHotelDetailDescription()
        fragInfo4.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fame1, fragInfo1).commit();
        fm.beginTransaction().replace(R.id.fame2, fragInfo2).commit();
        fm.beginTransaction().replace(R.id.fame3, fragInfo3).commit();
        fm.beginTransaction().replace(R.id.fame4, fragInfo4).commit();
    }

}