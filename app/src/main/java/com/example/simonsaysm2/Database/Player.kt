package com.example.simonsaysm2.Database

import android.app.Application
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Players")
data class Player(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name:String,
    val score: Long,
    val difficulty: String,
    val time: String
){
    constructor(name: String, score: Long, difficulty: String, time: String) : this(0, name, score, difficulty, time)
}