package com.openclassrooms.realestatemanager.hilt.module

import android.content.Context
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateRepository
import com.openclassrooms.realestatemanager.room.LoginLocalDB
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
 class EstatesRepository @Inject constructor( private val  login: LoginLocalDB): EstateRepository {
    override fun getAllEstates(): LiveData<List<Estate>> {
       return login.DaoAccess().getAllEstates()
    }

    override suspend fun insertEstate(estate: Estate) {
        return login.DaoAccess().insertEstate(estate)
    }




    //suspend fun insertEstate(estate: Estate) =login.DaoAccess().insertEstate(estate)


    override fun getOneEstate(value: String) {
        login.DaoAccess().getEstate(value)
    }

    override fun deleteEstate() {
        TODO("Not yet implemented")
    }

    override fun modifyEstate(estate: Estate) {
        return login.DaoAccess().updateEstate(estate)
    }
    //fun getAllEstates()= loginLocalDB.DaoAccess().getAllEstates()
    //fun getEstate( value: String?)= loginLocalDB.DaoAccess().getEstate(value)




}