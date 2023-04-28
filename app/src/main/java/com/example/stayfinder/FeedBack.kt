package com.example.stayfinder



data class FeedBack(
    var username: String,
    var avarta: String,
    var title: String,
    var content: String,
    var reviewDate: String,
    var rating: Double,
):java.io.Serializable{
    constructor(r: Review): this(r.user.display_name,r.user.photoUrl,r.title,r.comment,r.review_date,r.rating_overall)
//    constructor(r: Review):this(r.user.display_name,r.user.photoUrl,r.title,r.content,r.review_date,r.rating_overall)
}
