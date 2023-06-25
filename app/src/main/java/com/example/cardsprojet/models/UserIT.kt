package com.example.cardsprojet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserIT(
    @PrimaryKey val id_user: Int?,
    @ColumnInfo(name = "firstname") val firstname : String,
    @ColumnInfo(name = "lastname") val lastname : String,
    @ColumnInfo(name = "address") val address : String,
    @ColumnInfo(name = "username")val username : String?,
    @ColumnInfo(name = "image")val image : String?,
    @ColumnInfo(name = "phone")val phone : String?,
    @ColumnInfo(name = "mail")val mail : String?,

)
