package com.example.stayfinder

import java.io.Serializable
import java.net.URL

data class hotels(var id: String,
                  var hotel_name: String,
                  var pricebernight: Double,
                  var img: ArrayList<URL>,
                  var rating_overall: Double,
                  var address: address,
                  var description: String,
                  var noFeedback: Int,
                  val booking_count: Int,
                  var facilities: ArrayList<facilities>,
                  val comment_count: Int):Serializable{
    constructor(id: String,hotel_name: String,pricebernight: Double, address: address,img: ArrayList<URL>,rating_overall: Double,description: String):
            this(id,hotel_name,pricebernight,img,rating_overall,address,description,0,0,ArrayList<facilities>(),0)
}
data class address ( val number: Int, val street: String, val district: String, val ward: String,val city: String): Serializable
data class facilities(val name: String, val icon: URL)
data class rating(val cleanliness: Double, val comfort: Double, val services: Double, val locaiton: Double)