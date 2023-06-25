package com.example.cardsprojet.models

data class Restaurant(
    val id_resto:Int,
    val name:String,
    val location:String,
    val rating_restaurant: Float,
    var image:String,
    val facebook_url:String,
    val instagram_url:String,
    val phone:String,
    val type:Int,
    val type_resto:type_resto,
    val longitude: Double,
    val latitude: Double

)
