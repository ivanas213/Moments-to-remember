package com.example.trenucizapamcenje.models

data class User (
    val _id:String,
    val username: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val role: String,
    val address: String,
    val phone_number: String
)
