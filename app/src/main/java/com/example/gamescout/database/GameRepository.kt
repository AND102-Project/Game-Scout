package com.example.gamescout.database

import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {

    val allGames: Flow<List<GameEntity>> = gameDao.getAll()

    suspend fun insert(game: GameEntity) {
        gameDao.insert(game)
    }

    suspend fun insertAll(games: List<GameEntity>) {
        gameDao.insertAll(games)
    }

    suspend fun deleteAll() {
        gameDao.deleteAll()
    }

    suspend fun getAll() = gameDao.getAll()

    suspend fun insertDummyData() {
        val dummyGames = generateDummyData()
        insertAll(dummyGames)
    }
    fun generateDummyData(): List<GameEntity> {
        val dummyData = mutableListOf<GameEntity>()
        for (i in 1..10) {
            dummyData.add(
                GameEntity(
                    gameID = "Game $i",
                    steamAppID = "Description $i",
                    cheapest = "$i.99",
                    cheapestDealID = "$i.99",
                    external = "https://example.com/image$i.jpg",
                    internalName = "steamAppID$i",
                    thumb = "storeID$i"
                )
            )
        }
        return dummyData
    }
}