package com.example.myapplication

import com.example.trenucizapamcenje.models.Appointment
import com.example.trenucizapamcenje.models.AppointmentRequest
import com.example.trenucizapamcenje.models.Event
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.models.Promotion
import com.example.trenucizapamcenje.models.Rating
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.models.dto.AddRatingRequest
import com.example.trenucizapamcenje.models.dto.AppointRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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
    @PUT("/user/updateData/{userId}")
    fun updateData(
        @Path("userId") userId: String,
        @Body map: HashMap<String, String>
    ): Call<User>
    @PUT("/user/updateData/{userId}")
    fun changePassword(
        @Path("userId") userId: String,
        @Body map: HashMap<String, String>
    ): Call<User>
    @POST("/appointment/add")
    fun addToCart(
        @Body request: AppointmentRequest
    ): Call<Appointment>
    @GET("/appointment/getCart/{userId}")
    fun getCart(@Path("userId") userId: String) : Call<List<Appointment>>

    @DELETE("/appointment/delete/{appointmentId}")
    fun deleteAppointment(@Path("appointmentId") appointmentId: String): Call<Unit>
    @POST("/rating/add")
    fun addRating(@Body request: AddRatingRequest): Call<Rating>

    @GET("/rating/all/{offerId}")
    fun getAllRatings(@Path("offerId") offerId: String): Call<List<Rating>>

    @GET("/appointment/getNotifications/{userId}")
    fun getNotifications(@Path("userId") userId: String): Call<List<Appointment>>

    @POST("/appointment/appoint")
    fun appoint(@Body request: AppointRequest): Call<Unit>


}

