package com.example.digidentitytestapp.data.repository

import com.example.digidentitytestapp.data.mapper.ItemMapper
import com.example.digidentitytestapp.data.network.CatalogApi
import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.domain.repository.CatalogRepository

class CatalogRepositoryImpl(
    private val api: CatalogApi,
    private val mapper: ItemMapper
) : CatalogRepository {

    override suspend fun loadItems(sinceId: String?, maxId: String?): List<Item> {
        return api.getItems(sinceId, maxId).map(mapper::mapToDomain)
    }
}