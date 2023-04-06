package com.example.stayfinder

import java.net.URL

data class FeedBack(
    var username: String,
    var avarta: URL,
    var title: String,
    var content: String,
    var reviewDate: String,
    var rating: Double,
    var room_type: String,
    var checkin: String,
    var period: Int ,
    var nopeople: String
)