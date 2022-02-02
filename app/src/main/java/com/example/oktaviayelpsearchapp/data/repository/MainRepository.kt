package com.example.oktaviayelpsearchapp.data.repository

import com.example.oktaviayelpsearchapp.data.api.ApiHelper
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {

    fun getUsers(): Single<BusinessResponse> {
        return apiHelper.getUsers()
    }

}