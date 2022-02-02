package com.example.oktaviayelpsearchapp.data.repository

import com.example.oktaviayelpsearchapp.data.api.ApiHelper
import com.example.oktaviayelpsearchapp.data.model.Businesses
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {

    fun getUsers(): Single<List<Businesses>> {
        return apiHelper.getBusiness()
    }

}