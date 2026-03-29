package com.example.trenucizapamcenje.models

data class Rating (
    val user: User,
    val offer: Offer,
    val value: Int,
    val comment: String
)