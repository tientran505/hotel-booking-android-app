package com.example.stayfinder.model

import com.google.firebase.Timestamp
import kotlinx.serialization.Contextual

data class  FacilityModel (
    var icon:String?=null,
    var id:String?=null,
    @Contextual
    var last_update: Timestamp? = null,
    var name: String?=null
)