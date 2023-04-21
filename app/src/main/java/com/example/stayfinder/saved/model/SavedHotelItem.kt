package com.example.stayfinder.saved.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Contextual
import java.util.Date

@kotlinx.serialization.Serializable
data class SavedHotelItem(
    val id: String,
    val user_id: String,
    val collection_id: String,
    @Contextual
    val last_update: Timestamp = Timestamp.now()
    // val hotel_id: String
) {
    constructor() : this("", "", "")
}