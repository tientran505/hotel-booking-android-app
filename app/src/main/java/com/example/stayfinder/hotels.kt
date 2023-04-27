package com.example.stayfinder

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.net.URL

@IgnoreExtraProperties
data class hotels(
    var id: String = "",
    var hotel_name: String = "",
    var description: String = "",
    var address: address = address(),
    var photoUrl: ArrayList<String> = arrayListOf(),
    var facilities: ArrayList<facilities> = arrayListOf(),
    var rating: rating = rating(),
    var rating_overall: Double = 0.0,
    val booking_count: Int = 0,
    val comment_count: Int = 0,
) :Serializable{
    constructor(hotel_id:String, hotel_name:String, description:String, address:address, photoUrl: ArrayList<String>,
                facilities: ArrayList<facilities>) : this(hotel_id, hotel_name, description, address,
        photoUrl, facilities, rating(0.0,0.0,0.0,0.0,), 0.0,
        0, 0)
}

@IgnoreExtraProperties
data class address ( val number: String = "", val street: String = "", val district: String = "",
                     val ward: String = "",val city: String = ""): Serializable
@IgnoreExtraProperties
data class facilities(val id: Int = 0,val name: String = "", val icon: String = "")
@IgnoreExtraProperties
data class rating(val cleanliness: Double = 0.0, val comfort: Double = 0.0,
                  val services: Double = 0.0, val location: Double = 0.0)

@IgnoreExtraProperties
data class Review(
    var id: String = "",
    var user: String = "",
    var hotel_id:String = "",
    var review_date:String = "",
    var rating:rating = rating(),
    var rating_overall:Double = 0.0,
): Serializable {
}