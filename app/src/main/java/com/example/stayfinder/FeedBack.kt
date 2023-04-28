package com.example.stayfinder

import com.example.stayfinder.hotel.hotel_detail.hotelss
import com.example.stayfinder.hotel.hotel_detail.reviewss
import com.example.stayfinder.user.User
import java.io.Serializable
import java.net.URL

data class FeedBack(
    var username: String,
    var avarta: String,
    var title: String,
    var content: String,
    var reviewDate: String,
    var rating: Double,
):java.io.Serializable{
    constructor(r: reviewss): this(r.user.display_name,r.user.photoUrl,r.title,r.content,r.review_date,r.rating_overall)
//    constructor(r: Review):this(r.user.display_name,r.user.photoUrl,r.title,r.content,r.review_date,r.rating_overall)
}
