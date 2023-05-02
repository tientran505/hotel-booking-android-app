package com.example.stayfinder

import java.net.URL

data class rooms(
    var id: Int,
    var hotel_id: Int,
    var room_type: room_type,
    var description: String,
    var photoUrl: ArrayList<URL>,
    var available_start_date: String,
    var origin_price: Double,
    var discount_price: Double,
    var percentage_discount: Double,
    var applied_coupon: Int
) {
}

data class coupons (
    val coupons_id: Int,
    val title: String,
    val discount: Double,
    val start_date: String,
    val end_date: String,
        ){

}
enum class room_type {
    single, double, suite
}