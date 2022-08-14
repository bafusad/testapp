package com.example.digidentitytestapp.domain.usecase

import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.domain.repository.CatalogRepository

interface MainScreenUseCase {
    fun getCachedItems(): List<Item>
    suspend fun getInitialBatch(): List<Item>
    suspend fun lazyLoadItems(lastItemId: String): List<Item>
}

class MainScreenUseCaseImpl(
    private val cachingUseCase: CachingUseCase,
    private val repository: CatalogRepository,
) : MainScreenUseCase {

    override fun getCachedItems(): List<Item> {
        return cachingUseCase.getCache()
    }

    override suspend fun getInitialBatch(): List<Item> {
        val items = repository.loadItems(null, null)
        cachingUseCase.saveItems(items)
        return items
    }

    override suspend fun lazyLoadItems(lastItemId: String): List<Item> {
        return repository.loadItems(null, lastItemId)
    }
}