package com.example.stayfinder

import java.io.Serializable

data class HotelDetail (
    var id : String? = "",
    var title : String? = "",
    var price : Double? = 0.0,
    var type : String? = "",
    var img: String? = "",
): Serializable {
}