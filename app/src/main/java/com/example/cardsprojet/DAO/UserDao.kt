package com.example.cardsprojet.DAO


import androidx.room.Dao;
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query;
import com.example.cardsprojet.models.UserIT

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): UserIT

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserIT)

    @Query("DELETE FROM user")
    fun deleteUser()

    @Query("SELECT COUNT(*) FROM user")
    fun verifyLogged(): Int


}
