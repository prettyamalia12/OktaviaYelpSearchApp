package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.data.model.Businesses
import io.reactivex.Single

interface ApiService {
    fun getBusiness() : Single<List<Businesses>>
}