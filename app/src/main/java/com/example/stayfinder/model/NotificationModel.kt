package com.example.stayfinder.model

import java.io.Serializable

data class NotificationModel (
    var uuidUser: String? = null,
    var tokenUser: String? = null
) : Serializable