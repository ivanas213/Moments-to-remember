package com.example.trenucizapamcenje.models

data class Offer(
    val _id: String,
    val offerImageUrl: String,
    val name: String,
    val minGuests: Int,
    val maxGuests: Int,
    val price: Int,
    val hall: Hall                
)
