package com.example.stayfinder.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class RoomDetailModel(
    //activity add room detail 1
    var id:String? = null,
    var hotelId:String? = null,

    //activity add room detail 3
    var description: String? = null,
    var photoUrl:ArrayList<String> = ArrayList<String>(),

    //activity add room detail 2
    var available_start_date: Timestamp? = null,
    var origin_price: Double? = null,
    var discount_price:Double? = null,
    var percentage_discount: Double? = null,
    var applied_coupon_id: Int? = null,

    //Addition field, activity add room detail 1
    var room_type:String? = null,
    var have_room: ArrayList<String> = ArrayList(),
    var num_guest: Int? = null,
    var num_bedroom:Int? =null,
    var num_bathroom:Int? = null,
    var areaSquare:Double? = null

) :Serializable
