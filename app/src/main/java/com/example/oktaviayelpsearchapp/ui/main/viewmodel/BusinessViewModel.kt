package com.example.oktaviayelpsearchapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oktaviayelpsearchapp.data.model.AutocompleteResponse
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import com.example.oktaviayelpsearchapp.data.model.Businesses
import com.example.oktaviayelpsearchapp.data.repository.MainRepository

class BusinessViewModel : ViewModel() {

    private var businessLiveData: MutableLiveData<BusinessResponse>? = null
    private var autocompleteLiveData : MutableLiveData<AutocompleteResponse>? = null
    private var businessDetailsLiveData : MutableLiveData<Businesses>? = null
    private var searchBusinessLiveData : MutableLiveData<BusinessResponse>? = null

    fun searchBusiness(term:String, latitude: Double, longitude: Double) : LiveData<BusinessResponse>? {
        searchBusinessLiveData = MainRepository.searchBusinessApiCall(term, latitude, longitude)
        return searchBusinessLiveData
    }

    fun getBusiness(latitude: Double, longitude: Double) : LiveData<BusinessResponse>? {
        businessLiveData = MainRepository.getBusinessApiCall(latitude, longitude)
        return businessLiveData
    }

    fun autocompleteBusiness(text:String, latitude: Double, longitude: Double) : LiveData<AutocompleteResponse>? {
        autocompleteLiveData = MainRepository.getAutocompleteCall(text, latitude, longitude)
        return autocompleteLiveData
    }

    fun getBusinessDetails(id:String) : LiveData<Businesses>? {
        businessDetailsLiveData = MainRepository.getBusinessDetails(id)
        return businessDetailsLiveData
    }

}

