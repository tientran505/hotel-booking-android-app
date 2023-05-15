package com.example.stayfinder

import com.example.stayfinder.model.HotelDetailModel
import com.example.stayfinder.model.RoomDetailModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

@IgnoreExtraProperties
data class hotels(
    var id: String = "",
    @get:PropertyName("hotel_name")
    @set:PropertyName("hotel_name")
    var name: String = "",
    var owner_id: String ="",
    var description: String = "",
    var address: address = address(),
    var photoUrl: ArrayList<String> = arrayListOf(),
    var facilities: ArrayList<facilities> = arrayListOf<facilities>(),
    var rating: rating = rating(),
    var rating_overall: Double? = 0.0,
    var booking_count: Int = 0,
    var comment_count: Int = 0,
    var coupon_id: String = ""
) :Serializable{

}

@IgnoreExtraProperties
data class address ( val address: String = "", val city: String = ""): Serializable{
    override fun toString(): String {
        return super.toString()
    }
}
@IgnoreExtraProperties
data class facilities(val id: String ="",val name: String = "", val icon: String = ""):Serializable
@IgnoreExtraProperties
data class rating(val cleanliness: Double = 0.0, val comfort: Double = 0.0,
                  val services: Double = 0.0, val location: Double = 0.0)
    :Serializable{}

@IgnoreExtraProperties
data class Review(
    var id: String = "",
    var user: User_cmt = User_cmt(),
    var hotel_id:String = "",
    var review_date:String = "",
    var rating:rating = rating(),
    var rating_overall:Double = 0.0,
    var title:String="",
    var comment:String = "",
): Serializable {
}

@IgnoreExtraProperties
data class User_cmt(
    var user_id: String = "",
    var display_name: String = "",
    var photoUrl: String = "",
):Serializable{
}
@IgnoreExtraProperties
data class coupon(
    var id: String = "",
    var title: String = "",
    var discount: Double = 0.0,
    var startDate: String = "",
    var endDate: String = "",
    var owner_id: String = "",
):Serializable{


}
@IgnoreExtraProperties
data class rooms(
    var id: String="",
    var name:String="",
    var hotel_id: String="",
    var room_type: String ="",
    var description: String="",
    var photoUrl: ArrayList<String> = arrayListOf<String>(),
    var available_start_date: String="",
    var origin_price: Double = 0.0,
    var discount_price: Double=0.0,
    var percentage_discount: Double=0.0,
//    var applied_coupon: Int
)
:Serializable{

}

//@IgnoreExtraProperties
//data class booking_details(
//    var id:String="",
//    var startDate: String = "",
//    var endDate: String = "",
//    var num_of_days: Int=0,
//    var user_id : String = "",
//
//):Serializable{
//
//}

@IgnoreExtraProperties
data class room_information(
    var number_of_adult: Int=0,
    var number_of_children: Int=0,
    var number_of_rooms: Int=0,
    var rooms: ArrayList<String> = arrayListOf()
):Serializable{

}

@IgnoreExtraProperties
data class BookingDetail(
    var id: String = "",
    var created_date: Timestamp? = null,
    var status: String = "",
    var total_price: Double = 0.00,
    var date_start: Timestamp? = null,
    var date_end: Timestamp? = null,
    var num_of_nights: Int = 0,
    var user_id: String = "",
    var message: String = "",
    var personal_contact: ContactInformation? = null,
    var booking_information: BookingInformation? = null,
    var hotel: HotelDetailModel? = null,
    var rooms: ArrayList<RoomDetailModel> = arrayListOf()
) : Serializable{}

@IgnoreExtraProperties
data class ContactInformation(
    var name: String? = "",
    var email: String? = "",
    var phone_number: String? = ""
): Serializable{}

@IgnoreExtraProperties
data class BookingInformation(
    var number_of_adult: Int = 0,
    var number_of_children: Int = 0,
    var number_of_rooms: Int = 0,
    var sum_people: Int = 0,
): Serializable{
     fun display(): String {
        val adultStr: String = "${this.number_of_adult} adult" + if (this.number_of_adult > 1) "s" else ""
        val childStr: String = when (this.number_of_children) {
            0 -> "No child"
            1 -> "1 child"
            else -> "${this.number_of_children} children"
        }

        return "$adultStr - $childStr"
    }
}