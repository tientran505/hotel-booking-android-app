package com.example.stayfinder

import java.io.Serializable
import java.net.URL

data class address ( val number: Int, val street: String, val district: String, val ward: String,val city: String)
data class facilities(val name: String, val icon: URL)
class hotels() : Serializable {
    var id: String= ""
    var hotel_name: String? = ""
    var pricebernight: Double? = 0.0
    lateinit var img: ArrayList<URL>
    var favor: Boolean = false
    var rating_overall: Double? = 0.0
    lateinit var address: address
    var description: String? =""
    var noFeedback: Int= 0
    val booking_count = 0
    var facilities = ArrayList<facilities>()
    val comment_count = 0
    constructor(id:String,titlename: String, pricebernight: Double,address: address, img: ArrayList<URL>, favor: Boolean,rating: Double,descript: String) : this()
    {
        this.id = id
        this.hotel_name = titlename
        this.pricebernight= pricebernight
        this.img=img
        this.favor = favor
        this.address = address
        this.description = descript
        this.rating_overall = rating
    }
    constructor(id:String,titlename: String, pricebernight: Double,address: address, img: ArrayList<URL>, favor: Boolean,rating: Double,descript: String,noFeadBack:Int) : this()
    {
        this.id = id
        this.hotel_name = titlename
        this.pricebernight= pricebernight
        this.img=img
        this.favor = favor
        this.address = address
        this.description = descript
        this.rating_overall = rating
        this.noFeedback = noFeadBack
    }
}