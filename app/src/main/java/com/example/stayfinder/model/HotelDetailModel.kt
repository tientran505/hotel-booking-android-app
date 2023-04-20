package com.example.stayfinder.model

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


    )