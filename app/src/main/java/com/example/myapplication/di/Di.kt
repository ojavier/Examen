package com.example.myapplication.di

import com.example.myapplication.data.remote.SudokuApiService
import com.example.myapplication.data.remote.SudokuRemoteDataSource
import com.example.myapplication.ApiKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val apiKey = ApiKeyProvider.API_KEY
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Api-Key", apiKey)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideSudokuApiService(retrofit: Retrofit): SudokuApiService {
        return retrofit.create(SudokuApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSudokuRemoteDataSource(api: SudokuApiService): SudokuRemoteDataSource {
        return SudokuRemoteDataSource(api)
    }
}
