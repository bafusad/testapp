package com.example.digidentitytestapp.domain.repository

import com.example.digidentitytestapp.domain.entity.Item
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    suspend fun loadItems(sinceId: String?, maxId: String?): List<Item>
}