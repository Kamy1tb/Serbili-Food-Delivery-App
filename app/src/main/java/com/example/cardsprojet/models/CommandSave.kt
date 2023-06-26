package com.example.cardsprojet.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CommandSave (
     val id_command: Int?,
     val nom : String,
     val prix : Float,
     val  typeResto: String,
     val date: String,
     val heure:String,
     val status:Int,
     val image:String
        )
