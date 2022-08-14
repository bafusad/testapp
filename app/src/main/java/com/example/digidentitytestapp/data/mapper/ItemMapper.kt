package com.example.digidentitytestapp.data.mapper

import com.example.digidentitytestapp.data.entity.CatalogItem
import com.example.digidentitytestapp.domain.entity.Item

// TODO: extract to interface
class ItemMapper {

    fun mapToDomain(catalogItem: CatalogItem): Item {
        return Item(
            id = catalogItem.id ?: "",
            text = catalogItem.text ?: "",
            confidence = catalogItem.confidence ?: 0.0,
            image = catalogItem.image ?: ""
        )
    }

    fun mapToData(item: Item): CatalogItem {
        return CatalogItem(
            text = item.text,
            confidence = item.confidence,
            image = item.image,
            id = item.id
        )
    }
}