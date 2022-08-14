package com.example.digidentitytestapp.presentation.screen.main.adapter

import com.example.digidentitytestapp.domain.entity.Item

sealed class MainCell {

    data class ListItem(val item: Item) : MainCell()

    object Loading : MainCell()
}