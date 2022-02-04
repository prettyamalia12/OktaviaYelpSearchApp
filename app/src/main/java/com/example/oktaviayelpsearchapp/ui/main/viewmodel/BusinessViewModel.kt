package com.example.oktaviayelpsearchapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oktaviayelpsearchapp.data.model.AutocompleteResponse
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import com.example.oktaviayelpsearchapp.data.repository.MainRepository

class BusinessViewModel : ViewModel() {

    var businessLiveData: MutableLiveData<BusinessResponse>? = null
    var searchLiveData : MutableLiveData<AutocompleteResponse>? = null

    fun getBusiness(latitude: Double, longitude: Double) : LiveData<BusinessResponse>? {
        businessLiveData = MainRepository.getBusinessApiCall(latitude, longitude)
        return businessLiveData
    }

    fun searchBusiness(text:String, latitude: Double, longitude: Double) : LiveData<AutocompleteResponse>? {
        searchLiveData = MainRepository.getAutocompleteCall(text, latitude, longitude)
        return searchLiveData
    }
}

