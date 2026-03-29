package com.example.trenucizapamcenje.models

data class AppointmentRequest(
    val status: String = "cart",
    val date: String,
    val offer: String,
    val user: String,
    val guests: Int
)
