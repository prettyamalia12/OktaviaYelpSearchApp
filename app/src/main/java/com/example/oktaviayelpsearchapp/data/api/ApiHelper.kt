package com.example.oktaviayelpsearchapp.data.api

class ApiHelper(private val apiService: ApiService) {

    fun getUsers() = apiService.getBusiness()

}