package com.openclassrooms.realestatemanager.Retrofit

import com.openclassrooms.realestatemanager.Models.Places
import com.openclassrooms.realestatemanager.hilt.module.EstatesRepository
import com.openclassrooms.realestatemanager.repositories.GMapPlaceImpl
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GMapRepository @Inject constructor(private val apiInterface: APIInterface?) {
      fun getGMapPlace(): Call<Places> = apiInterface!!.GetMapPlaces()
      fun getMapPlaceDetails(place_id: String, key: String): Call<Object> = apiInterface!!.getMapPlaceDetails(place_id, key)

}