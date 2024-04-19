package com.example.gamescout.item_data

import android.app.Application

class GameApplication : Application() {

    val db by lazy { AppDatabase.getInstance(this) }

    val repository: GameRepository by lazy {
        GameRepository(db.gameDao())
    }


}

