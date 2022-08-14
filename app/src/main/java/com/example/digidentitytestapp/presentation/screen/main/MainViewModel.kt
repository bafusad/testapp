package com.example.digidentitytestapp.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.domain.usecase.MainScreenUseCase
import com.example.digidentitytestapp.presentation.screen.main.adapter.MainCell
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class MainViewModel(
    private val useCase: MainScreenUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState

    private val requestExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        _uiState.update { state ->
            state
                .removeLoadingCell { }
                .copy(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Something went wrong"
                )
        }
    }

    init {
        displayCachedItems()
        loadInitialItemsBatch()
    }

    private fun displayCachedItems() {
        val cachedCells = useCase.getCachedItems().map { MainCell.ListItem(it) }
        val cells = cachedCells.ifEmpty { listOf(MainCell.Loading) }
        _uiState.update { MainScreenState(cells = cells, isLoading = true) }
    }

    fun onSwipeRefresh() {
        reloadItems()
    }

    fun onRetry() {
        reloadItems()
    }

    private fun reloadItems() {
        _uiState.update {
            MainScreenState(
                cells = listOf(MainCell.Loading),
                isSwipeRefreshing = false,
                isLoading = true
            )
        }
        loadInitialItemsBatch()
    }

    private fun loadInitialItemsBatch() {
        (viewModelScope + requestExceptionHandler).launch {
            val items = useCase.getInitialBatch()
            val cells = items.map { MainCell.ListItem(it) }
            _uiState.update {
                MainScreenState(
                    cells = cells,
                    canLoadMore = items.isFullBatch(),
                    isLoading = false
                )
            }
        }
    }

    fun onRecyclerViewScrolled(lastVisibleItemPosition: Int) {
        _uiState.value.let { state ->
            if (state.canLoadMore
                && !state.isLoading
                && state.isLastCell(lastVisibleItemPosition)
            ) {
                lazyLoadItems(state.getLastItemId())
            }
        }
    }

    private fun lazyLoadItems(lastItemId: String?) {
        lastItemId?.let {
            addLoadingCell()

            (viewModelScope + requestExceptionHandler).launch {
                val items = useCase.lazyLoadItems(lastItemId)
                val newItemCells = items.map { MainCell.ListItem(it) }

                _uiState.update { state ->
                    val cells = ArrayList(state.cells).apply {
                        removeAll { it is MainCell.Loading }
                        addAll(newItemCells)
                    }
                    MainScreenState(
                        cells = cells,
                        canLoadMore = items.isFullBatch(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun addLoadingCell() {
        _uiState.update { state ->
            state
                .removeLoadingCell { cells -> cells.add(MainCell.Loading) }
                .copy(isLoading = true)
        }
    }

    private fun List<Item>.isFullBatch() = size == ITEMS_PER_REQUEST

    private inline fun MainScreenState.removeLoadingCell(
        onLoadingCellRemoved: ((MutableList<MainCell>) -> Unit)
    ): MainScreenState {
        val cells = cells.toMutableList().apply {
            removeAll { it is MainCell.Loading }
            onLoadingCellRemoved(this)
        }
        return copy(cells = cells)
    }

    companion object {
        private const val ITEMS_PER_REQUEST = 10
    }
}