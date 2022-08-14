package com.example.digidentitytestapp.data.entity

import com.google.gson.annotations.SerializedName

data class CatalogItem(
    @SerializedName("text") val text: String?,
    @SerializedName("confidence") val confidence: Double?,
    @SerializedName("image") val image: String?,
    @SerializedName("_id") val id: String?,
)
