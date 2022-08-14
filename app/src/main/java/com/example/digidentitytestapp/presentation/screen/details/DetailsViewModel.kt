package com.example.digidentitytestapp.presentation.screen.details

import androidx.lifecycle.ViewModel
import com.example.digidentitytestapp.domain.entity.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DetailsViewModel(savedState: SavedStatehandle) : ViewModel() {

    // val uiItem = savedState.getParcelabele ?: error("")

    private val _uiItem = MutableStateFlow(DetailsUIState())
    val uiItem: StateFlow<DetailsUIState> = _uiItem

    fun init(item: Item) {
        _uiItem.update { DetailsUIState(item) }
    }
}