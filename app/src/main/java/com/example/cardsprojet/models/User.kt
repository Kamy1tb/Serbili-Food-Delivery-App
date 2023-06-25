package com.example.cardsprojet.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id_user")
    val id_user: Int? = null,
    @SerializedName("first_name")
    val first_name: String? = null,
    @SerializedName("last_name")
    val last_name: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("mail")
    val mail: String? = null,
    @SerializedName("address")
    val address : String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("profile_pic")
    val profile_pic: String? = null
)