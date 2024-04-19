package com.example.gamescout.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "gameID") val gameID: String?,
    @ColumnInfo(name = "steamAppID") val steamAppID: String?,
    @ColumnInfo(name = "cheapest") val cheapest:String?,
    @ColumnInfo(name = "cheapestDealID") val cheapestDealID: String?,
    @ColumnInfo(name = "external") val external: String?,
    @ColumnInfo(name = "internalName") val internalName: String?,
    @ColumnInfo(name = "thumb") val thumb: String?,
)

