package com.example.stayfinder

import java.io.Serializable
import java.net.URL

data class hotels(
    var id: String,
    var hotel_name: String,
    var description: String,
    var address: address,
    var photoUrl: String,
    var facilities: ArrayList<facilities>,
) :Serializable{
    var rating: rating = rating(0.0,0.0,0.0,0.0)
    var rating_overall: Double = 0.0
    val booking_count: Int = 0
    val comment_count: Int =0

}
data class address ( val number: Int, val street: String, val district: String, val ward: String,val city: String): Serializable
data class facilities(val name: String, val icon: URL)
data class rating(val cleanliness: Double, val comfort: Double, val services: Double, val locaiton: Double)