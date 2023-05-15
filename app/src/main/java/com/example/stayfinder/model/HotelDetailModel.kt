package com.example.stayfinder.model

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class HotelDetailModel (

    var owner_id:String? = null,
    var id: String? = null,
    var applied_coupon: String? = null,
    var hotel_name: String? = null,
    var description: String? = null,
    var rating: HashMap<String, Int> = hashMapOf(
        "cleanliness" to 0,
        "comfort" to 0,
        "services" to 0,
        "location" to 0
    ),
    var rating_overall: Double? = null,
    var address: HashMap<String, String> = hashMapOf(
        "address" to "",
        "city" to ""
    ),
    var photoUrl: ArrayList<String> = ArrayList<String>(),
    var booking_count: Int = 0,
    var facilities: ArrayList<Objects> = ArrayList<Objects>(),
    var comment_count: Int = 0,
    var map:ArrayList<Double> = ArrayList()

) : Serializable {
    suspend fun getMinPriceOfHotel(currentGuest: Int): Double? {
        val db = Firebase.firestore

        val roomsRef = db.collection("rooms")
        val querySnapshot = roomsRef.whereEqualTo("hotel_id", this.id)
            .get()
            .await()

        var minPrice: Double? = null

        for (document in querySnapshot.documents) {
            val room = document.toObject(RoomDetailModel::class.java)
            val availablePrice = room?.available_prices?.firstOrNull { it.num_of_guest >= currentGuest }

            if (availablePrice != null) {
                if (minPrice == null || availablePrice.price < minPrice) {
                    minPrice = availablePrice.price
                }
            }
        }

        return minPrice
    }

}