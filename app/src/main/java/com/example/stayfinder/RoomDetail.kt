package com.example.stayfinder

class RoomDetail {
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

    fun getSalePrice() : Double{
        return price!! * sale!!
    }
}