package com.example.baitap.data

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("idMeal")
    val idMeal: String,

    @SerializedName("strMeal")
    val strMeal: String,

    @SerializedName("strMealThumb")
    val strMealThumb: String,

    // This property is not from the API, so we add it manually.
    var price: Int? = null
)