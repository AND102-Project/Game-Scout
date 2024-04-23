package com.example.gamescout.item_data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class DealItem(
    val internalName: String?,
    val title: String?,
    val metacriticLink: String?,
    val dealID: String?,
    val storeID : String?,
    val gameID: String?,
    val cheapest : String?,
    val cheapestDealID: String?,
    val external : String?,
    val salePrice : String?,
    val normalPrice : String?,
    val isOnSale : String?,
    val savings : String?,
    val metacriticScore : String?,
    val steamRatingText: String?,
    val steamRatingPercent: String?,
    val steamRatingCount: String?,
    val steamAppID: String?,
    val releaseDate: String?,
    val lastChange: String?,
    val dealRating: String?,
    val thumb: String?
)