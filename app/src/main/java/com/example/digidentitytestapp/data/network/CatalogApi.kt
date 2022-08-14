package com.example.digidentitytestapp.data.network

import com.example.digidentitytestapp.data.entity.CatalogItem
import retrofit2.http.GET
import retrofit2.http.Query

interface CatalogApi {

    @GET("items")
    suspend fun getItems(
        @Query("since_id") sinceId: String?,
        @Query("max_id") maxId: String?,
    ): List<CatalogItem>
}