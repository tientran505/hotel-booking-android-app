package com.example.stayfinder.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class  HotelDetailModel (

    var owner_id:String? = null,
    var id: String? = null,
    var hotel_name: String? = null,
    var description: String? = null,
    var rating: HashMap<String, Int> = hashMapOf(
            "cleanliness" to 0,
            "comfort" to 0,
            "services" to 0,
            "location" to 0
        ),
    var rating_overall: Double? = null,
    var address: HashMap<String, String> = hashMapOf(
            "city" to "",
            "number" to "",
            "street" to "",
            "district" to "",
            "ward" to ""
        ),
    var photoUrl: ArrayList<String> = ArrayList<String>(),
    var booking_count: Int = 0,
    var facilities: ArrayList<Objects> = ArrayList<Objects>(),
    var comment_count: Int = 0,


    var room: HashMap<String, Any> = hashMapOf(
        "type_room" to  ArrayList<String>(),
        "num_guest" to 0,
        "num_bathroom" to 0,
        "num_bedroom" to 0,
        "area" to 0
    ),
    var map:ArrayList<Double> = ArrayList<Double>()

    ) : Serializable