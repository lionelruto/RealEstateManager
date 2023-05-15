package com.openclassrooms.realestatemanager.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.Models.Converters
import com.openclassrooms.realestatemanager.Models.Estate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@Database(entities = [Estate::class], version= 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LoginLocalDB: RoomDatabase(){

       companion object{
             //private lateinit var localDB: LoginLocalDB
           private var LOC_TAG: String? = LoginLocalDB::class.simpleName
              private var LOCK: Object= Object()
              private var DATABASE_NAME= "Estates"
           fun getvalue()= "hello boy"
              fun getInstance(context: Context): LoginLocalDB{
                     /*if (localDB== null){
                            synchronized(LOCK){
                                   Log.d(LOC_TAG, "creation de la base de donnée")
                                   localDB= Room.databaseBuilder(
                                          context.applicationContext,
                                          LoginLocalDB::class.java, "Estates"
                                   ).build()
                            }
                            Log.d(LOC_TAG, "fin de creation de la base de donnée")

                     }*/
                      return (Room.databaseBuilder(
                         context,
                         LoginLocalDB::class.java,
                         "ESTATES"
                     )
                         .fallbackToDestructiveMigration()
                         .allowMainThreadQueries()
                         .build())
              }
       }



       abstract fun DaoAccess(): EstateDAO

}





