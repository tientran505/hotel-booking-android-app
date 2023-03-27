package com.example.stayfinder

import android.os.Parcel
import android.os.Parcelable

class Booking() {
    var titlename: String? = ""
    var period: String? = ""
    var price: Double? = 0.0
    var status: String? = ""
    var img: Int? = null

    constructor(titlename: String, period: String, price: Double,status: String, img: Int) : this()
    {
        this.titlename = titlename
        this.period = period
        this.price= price
        this.status= status
        this.img=img
    }
    constructor(titlename: String):this(){
        this.titlename = titlename
    }
}