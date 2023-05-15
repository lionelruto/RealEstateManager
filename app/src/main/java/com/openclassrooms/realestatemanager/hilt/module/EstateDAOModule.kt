package com.openclassrooms.realestatemanager.hilt.module

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.Retrofit.APIInterface
import com.openclassrooms.realestatemanager.Retrofit.GMapRepository
import com.openclassrooms.realestatemanager.room.EstateDAO
import com.openclassrooms.realestatemanager.room.LoginLocalDB
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
 class EstateDAOModule {
    private  val BASE_URL: String = "https://maps.googleapis.com/"

    @Provides
    @Singleton
    fun appDatabaseInstance( @ApplicationContext context: Context): LoginLocalDB{
        return (databaseBuilder(context, LoginLocalDB::class.java, "ESTATES")
            .fallbackToDestructiveMigration()
            .build())
    }

    /*
    @Provides
    fun getDAO(loginLocalDB: LoginLocalDB): EstateDAO {
        return( loginLocalDB.DaoAccess())}

     */

    @Provides
    @Singleton
    fun ProvideLogginInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun ProvideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun createRetrofit(retrofit: Retrofit): APIInterface{
        return retrofit.create(APIInterface::class.java)
    }

}