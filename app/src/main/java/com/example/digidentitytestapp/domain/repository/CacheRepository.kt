package com.example.digidentitytestapp.domain.repository

import com.example.digidentitytestapp.domain.entity.Item

interface CacheRepository {
    fun saveItems(items: List<Item>)
    fun getCacheItems(): List<Item>
    fun isSecretKeyGenerated(): Boolean
    fun generateAndSaveKey()
}