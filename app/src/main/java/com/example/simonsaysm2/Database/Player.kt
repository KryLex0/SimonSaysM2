package com.example.simonsaysm2.Database

import android.app.Application
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Players")
data class Player(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name:String,
    val score: Long,
    val difficulty: String
){
    constructor(name: String, score: Long, difficulty: String) : this(0, name, score, difficulty)
}