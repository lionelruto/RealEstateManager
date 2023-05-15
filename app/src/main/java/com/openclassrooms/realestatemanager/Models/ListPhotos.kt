package com.openclassrooms.realestatemanager.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity (tableName ="Photos")
data class ListPhotos (
    @ColumnInfo(name= "imgUri")
    val imgUri: String,
    @ColumnInfo(name= "imgDescription")
    val imgDesctiption: String,
        ): Serializable
