package com.openclassrooms.realestatemanager.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GMapPlusCode(
    @SerializedName("compound_code")
    val compound_code: String,
    @SerializedName("global_code")
    val global_code: String): Serializable {

}
