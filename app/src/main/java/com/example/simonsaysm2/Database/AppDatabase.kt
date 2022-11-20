package com.example.simonsaysm2.Database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Player::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    companion object{
        fun get(application: Application) : AppDatabase {
            return Room.databaseBuilder(application, AppDatabase::class.java, "Players").allowMainThreadQueries().build()
        }
    }
    abstract fun playerDao() : PlayerDao
}