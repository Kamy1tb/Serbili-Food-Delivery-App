package com.example.cardsprojet.DAO


import androidx.room.Dao;
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query;
import com.example.cardsprojet.models.CommandeIT

@Dao
interface CommandDao {
    @Query("SELECT * FROM commande")
    fun getAll(): List<CommandeIT>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(commande: CommandeIT)

    @Delete
    fun delete(commande: CommandeIT)

    @Query("DELETE FROM commande")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM commande")
    fun getCount(): Int

    @Query("SELECT SUM(prix * quantite) FROM commande")
    fun getTotal():Float



}
