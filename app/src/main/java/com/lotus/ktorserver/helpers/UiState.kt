package com.lotus.ktorserver.helpers

import com.lotus.ktorserver.models.Urun

sealed class UiState {
    object Loading : UiState()
    data class Success(val urunler: List<Urun>) : UiState()
    data class Error(val message: String) : UiState()
}