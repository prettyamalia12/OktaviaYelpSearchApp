package com.example.oktaviayelpsearchapp.data.model

import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("reviews")
    val reviews: List<Reviews>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("possible_languages")
    val possible_languages: List<String>,
)

data class Reviews(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("time_created")
    val time_created: String,
    @SerializedName("user")
    val user: User,
)

data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("profile_url")
    val profile_url: String,
    @SerializedName("image_url")
    val image_url: String,
    @SerializedName("name")
    val name: String,
)
