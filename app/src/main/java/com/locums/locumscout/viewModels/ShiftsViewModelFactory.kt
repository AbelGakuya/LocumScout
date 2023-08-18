package com.locums.locumscout.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.locums.locumscout.repo.FirebaseRepository

class ShiftsViewModelFactory(private val repository: FirebaseRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")

            return ShiftsViewModel(repository) as T
        }
        throw
                IllegalArgumentException("Unknown ViewModel Class")
    }
}