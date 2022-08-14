package com.example.digidentitytestapp.domain.usecase

import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.domain.repository.CacheRepository

interface CachingUseCase {
    fun getCache(): List<Item>
    fun saveItems(items: List<Item>)
}

class CachingUseCaseImpl(
    private val cacheRepository: CacheRepository
) : CachingUseCase {

    override fun getCache(): List<Item> {
        return cacheRepository.getCacheItems()
    }

    override fun saveItems(items: List<Item>) {
        if (cacheRepository.isSecretKeyGenerated()) {
            cacheRepository.saveItems(items)
        } else {
            cacheRepository.generateAndSaveKey()
            cacheRepository.saveItems(items)
        }
    }
}