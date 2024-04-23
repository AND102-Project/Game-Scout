package com.example.gamescout.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game_table")
    fun getAll(): Flow<List<GameEntity>>

    @Insert
    fun insertAll(games: List<GameEntity>)

    @Insert
    fun insert(GameEntity: GameEntity)

    @Query("DELETE FROM game_table")
    fun deleteAll()

    @Query("DELETE FROM game_table")
    fun delete()

    @Query("SELECT * FROM game_table WHERE gameID = :gameId")
    fun getGameById(gameId: String): GameEntity?

}