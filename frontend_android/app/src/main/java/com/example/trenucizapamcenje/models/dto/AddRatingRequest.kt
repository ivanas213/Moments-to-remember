package com.example.trenucizapamcenje.models.dto



data class AddRatingRequest (
    val user: String,
    val value: Int,
    val comment: String,
    val offer: String
)