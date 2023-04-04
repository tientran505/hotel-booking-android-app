package com.example.stayfinder

import java.net.URL

class FeedBack() {
    var username: String = ""
    lateinit var avarta: URL
    var title: String = ""
    var content: String = ""
    var reviewDate: String = ""
    var rating: Double = 0.0
    var room_type: String = ""
    var period: Int = 0
    var checkin: String=""
    var nopeople: String =""

    constructor(username: String,avarta: URL,title: String, content: String,reviewDate: String, rating: Double, room_type: String, checkin: String, period: Int, nopeople: String) : this()
    {
        this.username = username
        this.avarta = avarta
        this.title=title
        this.content = content
        this.reviewDate= reviewDate
        this.rating= rating
        this.room_type = room_type
        this.period = period
        this.checkin = checkin
        this.nopeople = nopeople
    }

}