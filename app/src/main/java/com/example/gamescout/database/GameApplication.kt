package com.example.gamescout.database

import android.app.Application
import com.example.gamescout.database.AppDatabase
import com.example.gamescout.database.GameRepository

class GameApplication : Application() {

    val db by lazy { AppDatabase.getInstance(this) }


    val repository: GameRepository by lazy {
        GameRepository(db.gameDao())
    }


}

