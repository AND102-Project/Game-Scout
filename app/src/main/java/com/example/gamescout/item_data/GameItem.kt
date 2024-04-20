package com.example.gamescout.item_data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameItem(
    val gameID: String?,
    val steamAppID: String?,
    val cheapest: String?,
    val cheapestDealID:String?,
    val external:String?,
    val internalName:String?,
    val thumb: String?,
) : Parcelable