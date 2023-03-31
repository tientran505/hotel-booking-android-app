package com.example.stayfinder

import android.os.Parcel
import android.os.Parcelable

class Booking() {
    var titlename: String? = ""
    var dateStart: String? = ""
    var dateEnd: String? = ""
    var price: Double? = 0.0
    var status: String? = ""
    var img: Int? = null

    constructor(titlename: String, dateStart: String,dateEnd: String, price: Double,status: String, img: Int) : this()
    {
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