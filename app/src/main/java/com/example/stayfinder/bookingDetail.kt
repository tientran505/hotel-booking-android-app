package com.example.stayfinder

import java.io.Serializable
import java.net.URL

class bookingDetail () : Serializable {
    var id: String= ""
    var titlename: String? = ""
    var pricebernight: Double? = 0.0
    lateinit var img: ArrayList<URL>
    var favor: Boolean = false
    var rating: Double? = 0.0
    var dateStart = ""
    var dateEnd = ""
    var address: String? =""
    var descript: String? =""
    var noFeedback: Int= 0
    constructor(id:String,titlename: String,dateStart:String, dateEnd:String, pricebernight: Double,address: String, img: ArrayList<URL>, favor: Boolean,rating: Double,descript: String) : this()
    {
        this.id = id
        this.titlename = titlename
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        this.pricebernight= pricebernight
        this.img=img
        this.favor = favor
        this.address = address
        this.descript = descript
        this.rating = rating
    }
    constructor(id:String,titlename: String,dateStart:String, dateEnd:String, pricebernight: Double,address: String, img: ArrayList<URL>, favor: Boolean,rating: Double,descript: String,noFeadBack:Int) : this()
    {
        this.id = id
        this.titlename = titlename
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        this.pricebernight= pricebernight
        this.img=img
        this.favor = favor
        this.address = address
        this.descript = descript
        this.rating = rating
        this.noFeedback = noFeadBack
    }
}