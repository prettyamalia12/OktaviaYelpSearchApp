package com.example.oktaviayelpsearchapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.oktaviayelpsearchapp.data.api.ApiClient
import com.example.oktaviayelpsearchapp.data.model.AutocompleteResponse
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import com.example.oktaviayelpsearchapp.data.model.Businesses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository {

    val businessResponse = MutableLiveData<BusinessResponse>()
    val searchResponse = MutableLiveData<BusinessResponse>()
    val autocompleteResponse = MutableLiveData<AutocompleteResponse>()
    val businessDetailsResponse = MutableLiveData<Businesses>()

    fun searchBusinessApiCall(term:String, latitude: Double, longitude: Double):
            MutableLiveData<BusinessResponse> {
        val call = ApiClient.apiInterface.searchBusiness(term, latitude, longitude)

        call.enqueue(object: Callback<BusinessResponse> {
            override fun onFailure(call: Call<BusinessResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<BusinessResponse>,
                response: Response<BusinessResponse>) {

                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()
                if (response.isSuccessful){
                    searchResponse.value = data
                }else{
                    //TODO error handling
                }
            }
        })

        return searchResponse

    }

    fun getBusinessApiCall(latitude: Double, longitude: Double): MutableLiveData<BusinessResponse> {

        val call = ApiClient.apiInterface.getBusiness(latitude, longitude)

        call.enqueue(object: Callback<BusinessResponse> {
            override fun onFailure(call: Call<BusinessResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<BusinessResponse>,
                response: Response<BusinessResponse>) {

                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()
                if (response.isSuccessful){
                    businessResponse.value = data?.businesses?.let { BusinessResponse(it) }
                }else{
                    //TODO error handling
                }
            }
        })

        return businessResponse
    }

    fun getAutocompleteCall(text:String, latitude: Double, longitude: Double):
            MutableLiveData<AutocompleteResponse> {

        val call = ApiClient.apiInterface.getAutocomplete(text, latitude, longitude)

        call.enqueue(object: Callback<AutocompleteResponse> {
            override fun onFailure(call: Call<AutocompleteResponse>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<AutocompleteResponse>,
                response: Response<AutocompleteResponse>) {

                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()
                if (response.isSuccessful){
                    autocompleteResponse.value = data
                }else{
                    // TODO error handling
                }
            }
        })
        return autocompleteResponse
    }

    fun getBusinessDetails(id:String): MutableLiveData<Businesses>{
        val call = ApiClient.apiInterface.getBusinessDetails(id)
        call.enqueue(object : Callback<Businesses>{
            override fun onResponse(call: Call<Businesses>, response: Response<Businesses>) {
                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()
                if (response.isSuccessful){
                    businessDetailsResponse.value = data
                }
                else{
                    // TODO error handling
                }
            }

            override fun onFailure(call: Call<Businesses>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }
        })

        return businessDetailsResponse
    }
}