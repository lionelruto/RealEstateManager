package com.openclassrooms.realestatemanager.Retrofit

import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Places
import dagger.Binds
import dagger.Provides
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.Objects


interface APIInterface {

    companion object{

    }

    @GET("maps/api/place/nearbysearch/json?radius=50000&types=restaurant&key=AIzaSyA-D0NXaMuK_aI6tfDf4bJIMt90KB61eiQ&location=-33.8670522,151.1957362")
    fun GetMapPlaces(): Call<Places>


    @GET("maps/api/place/details/json")
    fun getMapPlaceDetails(@Query("place_id") place_id:String, @Query("key") key: String): Call<Object>

}