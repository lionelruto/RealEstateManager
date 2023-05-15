package com.openclassrooms.realestatemanager.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MapGeometry(
    @SerializedName("location")
    val location: List<GMapLocation>,
    @SerializedName("viewport")
    val viewport: List<GMapViewport>):Serializable {

}

data class GMapViewport(
    @SerializedName("northeast")
    val northeast: List<GMapLocation>,

    @SerializedName("southwest")
    val southwest: List<GMapLocation>): Serializable {

}

data class GMapLocation (
    @SerializedName("lat")
    val lat: Int,
    @SerializedName("lng")
    val lng: Int):Serializable{

}
