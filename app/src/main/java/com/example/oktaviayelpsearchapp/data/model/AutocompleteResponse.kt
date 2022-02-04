package com.example.oktaviayelpsearchapp.data.model

import com.google.gson.annotations.SerializedName

data class AutocompleteResponse(
    @SerializedName("categories")
    val categories: List<Categories?>,
    @SerializedName("businesses")
    val businesses: List<Businesses?>,
    @SerializedName("terms")
    val terms: List<Terms?>
)

data class Terms(
    @SerializedName("text")
    val text: String
)
