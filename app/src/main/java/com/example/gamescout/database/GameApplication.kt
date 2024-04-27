package com.example.gamescout.database

import android.app.Application

class GameApplication : Application() {

    val db by lazy { AppDatabase.getInstance(this) }


    val repository: GameRepository by lazy {
        GameRepository(db.gameDao())
    }


}

