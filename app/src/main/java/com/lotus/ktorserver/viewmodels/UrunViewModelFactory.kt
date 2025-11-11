package com.lotus.ktorserver.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lotus.ktorserver.db.AppDatabase

class UrunViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UrunViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UrunViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}