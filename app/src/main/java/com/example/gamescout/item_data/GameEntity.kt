package com.example.gamescout.item_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.invoke.TypeDescriptor

@Entity(tableName = "game_table")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "salePrice") val salePrice: String?,
    @ColumnInfo(name = "normalPrice") val normalPrice: String?,
    @ColumnInfo(name = "thumb") val thumb: String?,
    @ColumnInfo(name = "steamAppID") val steamAppID: String?,
    @ColumnInfo(name = "storeID") val storeID: String?
)

