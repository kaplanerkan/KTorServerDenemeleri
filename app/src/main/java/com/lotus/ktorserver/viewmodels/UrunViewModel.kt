package com.lotus.ktorserver.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.helpers.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class UrunViewModel(private val database: AppDatabase) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun observeUrunler() {
        viewModelScope.launch {
            database.urunDao().getAllUrunlerFlow()
                .catch { _uiState.value = UiState.Error(it.message ?: "Hata") }
                .collect {urunler ->
                    Log.d("VIEWMODEL", "Yeni liste geldi: ${urunler.size} ürün")
                    _uiState.value = UiState.Success(urunler) }
        }
    }
}