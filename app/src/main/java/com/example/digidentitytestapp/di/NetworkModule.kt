package com.example.digidentitytestapp.di

import com.example.digidentitytestapp.data.network.CatalogApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun networkModule(): Module = module {
    single<CatalogApi> {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                val request: Request = it.request()
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", "3cc6223676937f269fd4ad2a2c85e838")
                    .build()
                it.proceed(newRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()
        Retrofit.Builder()
            .baseUrl("https://marlove.net/e/mock/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CatalogApi::class.java)
    }
    single { Gson() }
}