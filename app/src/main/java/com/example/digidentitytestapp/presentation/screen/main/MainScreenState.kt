package com.example.digidentitytestapp.presentation.screen.main

import com.example.digidentitytestapp.presentation.screen.main.adapter.MainCell

data class MainScreenState(
    val cells: List<MainCell> = emptyList(),
    val isLoading: Boolean = false,
    val isSwipeRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val errorMessage: String? = null
)

fun MainScreenState.isLastCell(index: Int) = index == cells.lastIndex

fun MainScreenState.getLastItemId(): String? {
    return (cells.findLast { it is MainCell.ListItem } as? MainCell.ListItem)?.item?.id
}