package com.example.cardsprojet.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cardsprojet.models.CommandeIT

@Database(entities = [CommandeIT::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun commandDao(): CommandDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,"serbili_cache").build()
                INSTANCE = instance
                return instance
            }
        }



    }
}