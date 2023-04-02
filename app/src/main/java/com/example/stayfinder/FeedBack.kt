package com.example.stayfinder

import java.net.URL

class FeedBack() {
    var username: String? = ""
    lateinit var avarta: URL
    var title: String? = ""
    var content: String? = ""
    var reviewDate: String? = ""
    var rating: Double? = 0.0
    lateinit var img : ArrayList<URL>

    constructor(username: String,avarta: URL,title: String, content: String,reviewDate: String, rating: Double, img : ArrayList<URL>) : this()
    {
        this.username = username
        this.avarta = avarta
        this.title=title
        this.content = content
        this.reviewDate= reviewDate
        this.rating= rating
        this.img=img
    }

}