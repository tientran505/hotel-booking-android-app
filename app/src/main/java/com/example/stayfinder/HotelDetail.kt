package com.example.stayfinder

class HotelDetail {
    var id : String? = ""
    var title : String? = ""
    var place : String? = ""
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
        title = stitle
        place = splace
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