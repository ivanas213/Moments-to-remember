package com.example.trenucizapamcenje.models


data class Appointment(

    val _id: String,
    val status: String,
    val date: String,
    val offer: Offer,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val user: User,
    val guests: Int
)
