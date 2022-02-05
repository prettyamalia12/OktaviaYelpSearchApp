package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.data.model.AutocompleteResponse
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import com.example.oktaviayelpsearchapp.data.model.Businesses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("businesses/search")
    fun getBusiness(@Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<BusinessResponse>

    @GET("businesses/search")
    fun searchBusiness(@Query ("term") term: String, @Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<BusinessResponse>

    @GET("autocomplete")
    fun getAutocomplete(@Query ("text") text: String, @Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<AutocompleteResponse>

    @GET("businesses")
    fun getBusinessDetails(@Query ("id") id: String) : Call<Businesses>

}