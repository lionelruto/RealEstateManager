package com.openclassrooms.realestatemanager.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GMapPhotos(
    @SerializedName("height")
    val height: Int,

    @SerializedName("html_attributions")
    val html_attributions: Array<String>,

    @SerializedName("photo_reference")
    val photo_reference: String,

    @SerializedName("width")
    val width: Int): Serializable {

}
