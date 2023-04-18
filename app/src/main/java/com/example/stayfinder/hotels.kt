package com.example.stayfinder

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.net.URL

@IgnoreExtraProperties
data class hotels(
    var hotel_id: String,
    var hotel_name: String,
    var description: String,
    var address: address,
    var photoUrl: ArrayList<URL>,
    var facilities: ArrayList<facilities>,
    var rating: rating,
    var rating_overall: Double,
    val booking_count: Int,
    val comment_count: Int,
) :Serializable{
    constructor(hotel_id:String, hotel_name:String, description:String, address:address, photoUrl: ArrayList<URL>,
                facilities: ArrayList<facilities>) : this(hotel_id, hotel_name, description, address,
        photoUrl, facilities, rating(0.0,0.0,0.0,0.0,), 0.0,
        0, 0)

    constructor():this("","","",address(0,"","","","")
        , arrayListOf(), arrayListOf(),rating(0.0,0.0,0.0,0.0),
        0.0,0,0)

    constructor(a:hotels):this(a.hotel_id,a.hotel_name,a.description,a.address,a.photoUrl,a.facilities,a.rating,
        a.rating_overall,a.booking_count,a.comment_count)
}
data class address ( val number: Int, val street: String, val district: String, val ward: String,val city: String): Serializable
data class facilities(val id: Int,val name: String, val icon: URL)
data class rating(val cleanliness: Double, val comfort: Double, val services: Double, val locaiton: Double)