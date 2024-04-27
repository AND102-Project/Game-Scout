package com.example.gamescout.cheapsharkAPI

import com.example.gamescout.item_data.DealItem
import com.example.gamescout.item_data.StoreItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApiService {
    @GET("deals")
    fun getDeals(): Call<List<DealItem>>

    @GET("deals")
    fun getDealId(
        @Query("id") id: String
    ) : Call<DealItem>

    @GET("deals")
    fun searchGames(
        @Query("title") name: String
    ): Call<List<DealItem>>

    @GET("stores")
    fun getStores(): Call<List<StoreItem>>

    @GET("alerts?action=set")
    fun setPriceAlert(
        @Query("email") email: String,
        @Query("gameID") gameID: String,
        @Query("price") price: String
    ): Call<Boolean>

    @GET("alerts?action=manage")
    fun manageAlerts(
        @Query("email") email: String
    ): Call<Void>
}
