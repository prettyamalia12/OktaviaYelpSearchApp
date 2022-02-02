package com.example.oktaviayelpsearchapp.data.api

import com.example.oktaviayelpsearchapp.data.model.Businesses
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

class ApiServiceImpl : ApiService {
    override fun getBusiness(): Single<List<Businesses>> {
        return Rx2AndroidNetworking.get("https://api.yelp.com/v3/businesses/search")
            .build()
            .getObjectListSingle(Businesses::class.java)
    }
}