package com.example.cardsprojet.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cardsprojet.models.type_menu
import com.google.type.DateTime
import java.sql.Time

@Entity(tableName = "commande")
data class CommandeIT(
    @PrimaryKey val id_menu: Int?,
    @ColumnInfo(name = "nom") val nom : String,
    @ColumnInfo(name = "prix") val prix : Float,
    @ColumnInfo(name = "quantite")val quantite : Int?,
    @ColumnInfo(name = "image")val image : String?

)
