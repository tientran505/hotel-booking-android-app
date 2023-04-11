package com.example.stayfinder

import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

class HotelDetail {
    var id : String? = ""
    var name : String? = ""
    var address : String? = ""
    var type : String? = ""
    var price : Double? = 0.0
    var sale : Double? = 1.0
    var description : String? = ""
    var point : Double? = 0.0
    var reviewers : Int? = 0
    var img: Int? = null

    constructor(sid: String, stitle: String, splace: String, stype: String, sprice: Double,
                ssale: Double, sdescription: String, spoint: Double, sreviewers: Int, simg: Int){
        id = sid
        name = stitle
        address = splace
        type = stype
        price = sprice
        sale = ssale
        description = sdescription
        point = spoint
        reviewers = sreviewers
        img = simg
    }
    fun getSalePrice() : Double{
        return price!! * (1-sale!!)
    }
}
@IgnoreExtraProperties
class Hotel{
    var id : String? = ""
    var name : String? = ""
    var address : ArrayList<String> = arrayListOf<String>("city", "number", "street", "district", "ward")
    var description : String? = ""
    var booking_count: Int = 0
    var rating : ArrayList<Double> = arrayListOf<Double>(0.0,0.0,0.0,0.0)
    var rating_overall : Double = 0.0
    var photoUrl : String? = ""

    constructor(){

    }

    constructor(a: Hotel){
        id = a.id
        name = a.name
        address = a.address
        description = a.description
        booking_count = a.booking_count
        rating = a.rating
        rating_overall = a.rating_overall
        photoUrl = a.photoUrl
    }
    constructor(stitle: String, sdescription: String){
        id = UUID.randomUUID().toString()
        name = stitle
        description = sdescription
    }

    override fun toString():String{
        return id.toString() + " " + name.toString()
    }
}