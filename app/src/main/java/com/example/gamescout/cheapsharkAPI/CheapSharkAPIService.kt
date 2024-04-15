package com.example.gamescout.cheapsharkAPI

import com.example.gamescout.item_data.GameItem
import retrofit2.Call
import retrofit2.http.GET

interface CheapSharkApiService {
    @GET("deals")
    fun getDeals(): Call<List<GameItem>>
}
