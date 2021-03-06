package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.data.model.AutocompleteResponse
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import com.example.oktaviayelpsearchapp.data.model.Businesses
import com.example.oktaviayelpsearchapp.data.model.ReviewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("businesses/search")
    fun getBusiness(@Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<BusinessResponse>

    @GET("businesses/search")
    fun searchBusiness(@Query ("term") term: String, @Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<BusinessResponse>

    @GET("autocomplete")
    fun getAutocomplete(@Query ("text") text: String, @Query ("latitude") latitude: Double, @Query ("longitude") longitude: Double) : Call<AutocompleteResponse>

    @GET("businesses/{id}")
    fun getBusinessDetails(@Path ("id") id: String) : Call<Businesses>

    @GET("businesses/{id}/reviews")
    fun getReviews(@Path ("id") id: String) : Call<ReviewsResponse>


}