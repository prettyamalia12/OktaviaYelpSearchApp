package com.example.oktaviayelpsearchapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import com.example.oktaviayelpsearchapp.data.repository.MainRepository

class BusinessViewModel : ViewModel() {

    var businessLiveData: MutableLiveData<BusinessResponse>? = null

    fun getBusiness(latitude: Double, longitude: Double) : LiveData<BusinessResponse>? {
        businessLiveData = MainRepository.getBusinessApiCall(latitude, longitude)
        return businessLiveData
    }
}

