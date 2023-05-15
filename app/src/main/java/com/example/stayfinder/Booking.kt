package com.example.stayfinder

import java.net.URL

class Booking() {
    var id: String? = ""
    var titlename: String? = ""
    var dateStart: String? = ""
    var dateEnd: String? = ""
    var price: Double? = 0.0
    var status: String? = ""
    lateinit var img : URL
    constructor(
        id: String,
        titlename: String, dateStart: String,
        dateEnd: String, price: Double,
        status: String, img: URL
    ) : this()
    {
        this.id = id
        this.titlename = titlename
        this.dateStart = dateStart
        this.dateEnd= dateEnd
        this.price= price
        this.status= status
        this.img=img
    }
    constructor(titlename: String):this(){
        this.titlename = titlename
    }
}