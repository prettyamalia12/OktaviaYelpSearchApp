package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("search")
    fun getBusiness(@Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<BusinessResponse>
}