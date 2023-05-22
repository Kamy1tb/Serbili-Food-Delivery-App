package com.example.cardsprojet

import com.example.cardsprojet.models.MenuData
import com.example.cardsprojet.models.Restaurant
import com.example.cardsprojet.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("restaurants")
    suspend fun getRestaurants(): Response<MutableList<Restaurant>>
    @POST("login")
    suspend fun signIn(@Body login : Map<String,String>):Response<User>
    @GET("menuByID")
    suspend fun menuByID(@Query("id_resto") restaurantId: Int):Response<List<MenuData>>

}