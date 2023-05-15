package com.openclassrooms.realestatemanager.Models

import androidx.annotation.NonNull
import androidx.room.*
import java.io.Serializable

@Entity( tableName= "estate")
data class Estate(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="Id")
    val Id: Int?,

    @ColumnInfo(name= "type")
    val type: String,

    @TypeConverters(ListPhotos::class)
    @ColumnInfo(name="photos")
    val photo: List<ListPhotos>,

    @ColumnInfo(name= "price")
    val price: Int,

    @ColumnInfo(name= "surface")
    val surface: Int,

    @ColumnInfo(name= "nbre_piece")
    val nbre_piece: Int,

    @ColumnInfo(name= "desc")
    val description: String,

    @ColumnInfo(name= "address")
    val adress: String,

    @TypeConverters(String::class)
    @ColumnInfo(name= "interet")
    val interet: List<String>,

    @ColumnInfo(name= "status")
    val status: Int,

    @ColumnInfo(name= "date_In")
    val date_In: String,

    @ColumnInfo(name= "date_sold")
    val date_sold: String,

    @ColumnInfo(name= "agent")
    val agent: String): Serializable


