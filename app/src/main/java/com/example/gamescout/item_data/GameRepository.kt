package com.example.gamescout.item_data


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
                    title = "Game $i",
                    description = "Description $i",
                    salePrice = "$i.99",
                    normalPrice = "$i.99",
                    thumb = "https://example.com/image$i.jpg",
                    steamAppID = "steamAppID$i",
                    storeID = "storeID$i"
                )
            )
        }
        return dummyData
    }
}