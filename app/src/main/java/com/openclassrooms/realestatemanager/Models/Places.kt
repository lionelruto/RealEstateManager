package com.openclassrooms.realestatemanager.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Places(
                  @SerializedName("business_status")
                  @Expose
                  val business_status: String,

                  @SerializedName("geometry")
                  @Expose
                  val geometry: MapGeometry,

                  @SerializedName("icon")
                  @Expose
                  val icon: String,

                  @SerializedName("name")
                  @Expose
                  val name: String,

                  @SerializedName("icon_background_color")
                  @Expose
                  val icon_background_color: String,

                  @SerializedName("icon_mask_base_uri")
                  @Expose
                  val icon_mask_base_uri: String,


                  @SerializedName("opening_hours")
                  @Expose
                  val opening_hours: String,

                  @SerializedName("photos")
                  @Expose
                  val photos: List<GMapPhotos>,

                  @SerializedName("place_id")
                  @Expose
                  val place_id: String,

                  @SerializedName("plus_code")
                  @Expose
                  val plus_code: List<GMapPlusCode>,

                  @SerializedName("price_level")
                  @Expose
                  val price_level: Int,

                  @SerializedName("rating")
                  @Expose
                  val rating: Double,

                  @SerializedName("reference")
                  @Expose
                  val reference: String,

                  @SerializedName("scope")
                  val scope: String,

                  @SerializedName("types")
                  @Expose
                  val types: List<String>,

                  @SerializedName("user_rating_total")
                  @Expose
                  val user_rating_total: Int,

                  @SerializedName("vicinity")
                  @Expose
                  val vicinity: String): Serializable{

}
