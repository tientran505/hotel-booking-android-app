package com.example.stayfinder.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.io.Serializable

data class RoomDetailModel(
    //activity add room detail 1
    var id: String = "",
    var hotel_id: String = "",

    var name: String = "",

    //activity add room detail 3
    var description: String = "",
    var photoUrl:ArrayList<String> = ArrayList(),
    var room_quantity: Number = 0,
    var room_available: Number = 0,
    var guest_available: Number = 0,
    var min_guest: Number = 0,
    var beds: ArrayList<Bed> = ArrayList(),
    var discount_type: String = "",

    //activity add room detail 2
    var available_start_date: Timestamp? = null,
    var origin_price: Double? = null,
    var discount_price:Double? = null,
    var percentage_discount: Double? = null,
    var applied_coupon_id: String? = null,

    //Addition field, activity add room detail 1
    var room_type:String? = null,
    var area_square:Double? = null,

    var facilities: ArrayList<String> = ArrayList(),

    var created_date: Any? = null
) :Serializable

data class Bed (
    var name: String = "",
    var quantity: Number = 0,
) : Serializable
