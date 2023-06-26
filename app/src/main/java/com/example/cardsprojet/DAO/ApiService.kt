package com.example.cardsprojet.DAO

import com.example.cardsprojet.models.CommandSave
import com.example.cardsprojet.models.CommandeIT
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
    @POST("signup")
    suspend fun signUp(@Body signup : Map<String,String>):Response<User>
    @GET("commandsByID")
    suspend fun CommandsById(@Query("id_user") userId: Int):Response<List<CommandSave>>
    @POST("newCommand")
    suspend fun newCommand(@Body command: Map<String,String>):Response<Int>
    @POST("newCommandItems")
    suspend fun newCommandItems(@Body commande: Map<String,String>):Response<Map<String,String>>
}