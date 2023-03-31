package com.example.stayfinder

import java.io.Serializable

class bookingDetail () : Serializable {
    var titlename: String? = ""
    var pricebernight: Double? = 0.0
    lateinit var img: ArrayList<Int>
    var favor: Boolean = false
//    var rating: Double? = 0.0
    var dateStart = ""
    var dateEnd = ""
    var address: String? =""
    var descript: String? =""
    constructor(titlename: String,dateStart:String, dateEnd:String, pricebernight: Double,address: String, img: ArrayList<Int>, favor: Boolean,rating: Double,descript: String) : this()
    {
        this.titlename = titlename
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        this.pricebernight= pricebernight
        this.img=img
        this.favor = favor
        this.address = address
        this.descript = descript

    }
//    constructor(titlename: String):this(){
//        this.titlename = titlename
//    }
}