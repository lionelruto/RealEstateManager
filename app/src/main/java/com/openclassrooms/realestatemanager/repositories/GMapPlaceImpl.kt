package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.Models.Places
import retrofit2.Call

interface GMapPlaceImpl {

     fun getGMapPlace(radius: Int, type: String,  location: String, key: String): Call<Places?>?
}