package com.openclassrooms.realestatemanager.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.hilt.module.EstatesRepository

class EstateFactory(private val estatesRepository: EstatesRepository ?) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T> ): T {
        return (estatesRepository?.let { EstateViewModel(it) } as T)
        /*if (modelClass.isAssignableFrom(First_Activity::class.java)){
            return EstateViewModel(estatesRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")

         */
    }
}