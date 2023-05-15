package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.Models.Estate

//Inutile, juste utilsé quand on veut faire de l'injection manuelle et sans room ou retrofit
interface EstateRepository {
     fun getAllEstates(): LiveData<List<Estate>>

     suspend fun insertEstate(estate: Estate)

     fun getOneEstate(value: String)

     fun deleteEstate()

     fun modifyEstate(estate: Estate)

}