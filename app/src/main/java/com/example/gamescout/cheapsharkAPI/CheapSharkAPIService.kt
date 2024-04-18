package com.example.gamescout.cheapsharkAPI

import com.example.gamescout.item_data.Dealtem
import com.example.gamescout.item_data.StoreItem
import retrofit2.Call
import retrofit2.http.GET

interface CheapSharkApiService {
    @GET("deals")
    fun getDeals(): Call<List<Dealtem>>

    @GET("stores")
    fun getStores(): Call<List<StoreItem>>
}
