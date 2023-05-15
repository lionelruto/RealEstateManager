package com.openclassrooms.realestatemanager.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.*
import com.openclassrooms.realestatemanager.Models.Estate

@Dao
interface EstateDAO {

    @Insert
    suspend fun insertEstate(estate: Estate)

    @Update (onConflict = OnConflictStrategy.REPLACE)
     fun updateEstate(estate: Estate)

    @Query("SELECT * FROM estate WHERE type=:value")
    fun getEstate(value:String?): Estate

    @Query("SELECT * FROM estate")
    fun getAllEstates(): LiveData<List<Estate>>

    @Query("SELECT * FROM estate WHERE id=:id")
    fun getAllEstatesCursor(id: Long): Cursor
}