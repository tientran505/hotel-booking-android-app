package com.example.stayfinder.booking.model

@kotlinx.serialization.Serializable
data class GuestForm(val name: String, val phone: String, val email: String)