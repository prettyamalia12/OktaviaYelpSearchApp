package com.example.oktaviayelpsearchapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.oktaviayelpsearchapp.data.api.ApiClient
import com.example.oktaviayelpsearchapp.data.model.BusinessResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository {

    val businessResponse = MutableLiveData<BusinessResponse>()

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

                businessResponse.value = data?.businesses?.let { BusinessResponse(it) }
            }
        })

        return businessResponse
    }

}