package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient{

    val BASE_URL = "https://api.yelp.com/v3/businesses/"
    val API_KEY = "6sgDINbMbdU0FrP0s3xw2FePy3T8orLHbkDuctBRwXIZZQ4bYOWkEcnB9vIs5JoK7lV7KMIDZBp6W7E9NNalPHhBXsXtuSDm0LUFcPlWaqC3pxa3_BuP-sMwXHP7YXYx"

    val retrofitClient: Retrofit.Builder by lazy {

        val levelType: Level = if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            Level.BODY else Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging).addInterceptor {
            val request: Request = it.request().newBuilder().addHeader("Authorization", "Bearer $API_KEY").build()
            return@addInterceptor it.proceed(request)
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}