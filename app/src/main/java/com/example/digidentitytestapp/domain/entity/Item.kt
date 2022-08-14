package com.example.digidentitytestapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String,
    val text: String,
    val confidence: Double,
    val image: String,
): Parcelable