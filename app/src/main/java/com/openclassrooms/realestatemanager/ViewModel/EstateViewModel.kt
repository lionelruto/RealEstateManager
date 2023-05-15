package com.openclassrooms.realestatemanager.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.hilt.module.EstatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor( private val estatesRepository: EstatesRepository)
    : ViewModel() {

    fun getEstate(value: String)= estatesRepository?.getOneEstate(value)

    fun updateEstate(value: Estate){
        estatesRepository?.modifyEstate(value)
    }
    fun getAllEstate(): LiveData<List<Estate>>? {
        return estatesRepository?.getAllEstates()
    }

    fun insertEstate(value: Estate){
           //estatesRepository.insertEstate(value)

        viewModelScope.launch {
            try {
                estatesRepository?.insertEstate(value)
                // here you have your CoursesFromDb
            } catch (e: Exception) {
                // handler error
            }
        }
        }


    /**
     *     companion object {

    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
    modelClass: Class<T>,
    extras: CreationExtras
    ): T {
    // Get the Application object from extras
    val application = checkNotNull(extras[APPLICATION_KEY])
    // Create a SavedStateHandle for this ViewModel from extras
    val savedStateHandle = extras.createSavedStateHandle()

    return EstateViewModel(
    (application as HiltApplication).estatesRepository,
    savedStateHandle
    ) as T
    }
    }
    }
     */


}