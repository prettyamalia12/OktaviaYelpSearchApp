package com.example.oktaviayelpsearchapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.oktaviayelpsearchapp.data.api.ApiClient
import com.example.oktaviayelpsearchapp.data.model.AutocompleteResponse
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository {

    val businessResponse = MutableLiveData<BusinessResponse>()
    val searchResponse = MutableLiveData<AutocompleteResponse>()

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
                    searchResponse.value = data
                }else{
                    // TODO error handling
                }
            }
        })
        return searchResponse
    }
}