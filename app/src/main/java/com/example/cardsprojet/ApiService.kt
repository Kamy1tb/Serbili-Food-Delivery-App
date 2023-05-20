package com.example.cardsprojet

import com.example.cardsprojet.models.Post
import com.example.cardsprojet.models.Restaurant
import com.example.cardsprojet.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import java.util.Objects

interface ApiService {



    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    @GET("restaurants")
    suspend fun getRestaurants(): Response<MutableList<Restaurant>>


    @POST("login")
    suspend fun signIn(@Body login : Map<String,String>):Response<User>
    @POST("TEST")
    suspend fun test(@Body login : Map<String,String>):Response<String>

}