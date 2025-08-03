package com.example.myapplication

import com.example.trenucizapamcenje.models.Event
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.models.Promotion
import com.example.trenucizapamcenje.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitInterface {

    @POST("/user/login")
    fun login(@Body map: HashMap<String, String>):Call<User>

    @GET("/promotion/getAll")
    fun getPromotions():Call<List<Promotion>>

    @GET("/event/getAll")
    fun getEvents():Call<List<Event>>

    @GET("/offer/getAll")
    fun getOffers():Call<List<Offer>>


}