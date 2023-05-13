package com.example.stayfinder.model

import java.io.Serializable

data class NotificationModel (
    var uuidUser: String?,
    var tokenUser: String?
) : Serializable