package com.example.oktaviayelpsearchapp.data.model

import com.google.gson.annotations.SerializedName

data class BusinessResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("businesses")
    val businesses: List<Businesses>,
    @SerializedName("region")
    val region: Region,
)

data class Businesses(
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("is_closed")
    val is_closed: Boolean,
    @SerializedName("categories")
    val categories: List<Categories>,
    @SerializedName("review_count")
    val review_count: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
    @SerializedName("image_url")
    val image_url: String,
    @SerializedName("location")
    val location: Location,
)

data class Categories(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("title")
    val title: String
)

data class Coordinates(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
)

data class Location(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("address2")
    val address2: String,
    @SerializedName("address3")
    val address3: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("title")
    val title: String
)

data class Region(
    @SerializedName("center")
    val center: Center,
)

data class Center(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
)
