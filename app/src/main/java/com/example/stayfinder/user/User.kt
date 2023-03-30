package com.example.stayfinder.user

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

data class User(
    var uid: String,
    var displayName: String? = null,
    var email: String? = null,
    var photoUrl: String? = null,
    val phone: String? = null,
    val isEmailVerified: Boolean? = false,
    val providerData: List<UserInfo>? = null
) {
    constructor(user: FirebaseUser) : this(user.uid, user.displayName, user.email,
        user.photoUrl.toString(), user.phoneNumber, user.isEmailVerified, user.providerData)
}