package com.example.stayfinder

import com.example.stayfinder.user.User
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.net.URL

@IgnoreExtraProperties
data class hotels(
    var id: String = "",
    var hotel_name: String = "",
    var owner_id: String ="",
    var description: String = "",
    var address: address = address(),
    var photoUrl: ArrayList<String> = arrayListOf(),
    var facilities: ArrayList<facilities> = arrayListOf<facilities>(),
    var rating: rating = rating(),
    var rating_overall: Double = 0.0,
    var booking_count: Int = 0,
    var comment_count: Int = 0,
) :Serializable{
    constructor(hotel_id:String, hotel_name:String,owner_id: String, description:String, address:address, photoUrl: ArrayList<String>,
                facilities: ArrayList<facilities>) : this(hotel_id, hotel_name, owner_id,description, address,
        photoUrl, facilities, rating(0.0,0.0,0.0,0.0,), 0.0,
        0, 0)
}

@IgnoreExtraProperties
data class address ( val number: String = "", val street: String = "", val district: String = "",
                     val ward: String = "",val city: String = ""): Serializable
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
data class coupons (
    val coupons_id: String ="",
    val title: String="",
    val discount: Double=0.0,
    val start_date: String="",
    val end_date: String="",
):java.io.Serializable{
}
@IgnoreExtraProperties
data class rooms(
    var id: String="",
    var hotel_id: String="",
    var room_type: String ="",
    var description: String="",
    var photoUrl: ArrayList<URL>,
    var available_start_date: String="",
    var origin_price: Double = 0.0,
    var discount_price: Double=0.0,
    var percentage_discount: Double=0.0,
//    var applied_coupon: Int
) {
}
