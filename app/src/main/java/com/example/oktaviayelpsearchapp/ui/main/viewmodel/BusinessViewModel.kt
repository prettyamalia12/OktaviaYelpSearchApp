package com.example.oktaviayelpsearchapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oktaviayelpsearchapp.data.model.Businesses
import com.example.oktaviayelpsearchapp.data.repository.MainRepository
import com.example.oktaviayelpsearchapp.utils.Resource
import com.example.oktaviayelpsearchapp.utils.Resource.Companion.success
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BusinessViewModel  (private val mainRepository: MainRepository) : ViewModel() {

    private val business = MutableLiveData<Resource<List<Businesses>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchBusiness()
    }

    private fun fetchBusiness() {
        business.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ businessList ->
                    business.postValue(success(businessList))
                },
                    { business.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getBusiness(): LiveData<Resource<List<Businesses>>>{
        return business
    }
}