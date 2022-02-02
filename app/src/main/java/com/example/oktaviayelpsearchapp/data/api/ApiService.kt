package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import io.reactivex.Single

interface ApiService {
    fun getBusiness() : Single<BusinessResponse>
}