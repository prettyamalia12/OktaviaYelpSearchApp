package com.example.oktaviayelpsearchapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.oktaviayelpsearchapp.data.model.ReviewsResponse
import com.example.oktaviayelpsearchapp.data.repository.MainRepository

class ReviewsViewModel : ViewModel() {
    private var reviewsLiveData: MutableLiveData<ReviewsResponse>? = null

    fun getReviews(id: String) : LiveData<ReviewsResponse>? {
        reviewsLiveData = MainRepository.getReviewsApiCall(id)
        return reviewsLiveData
    }
}