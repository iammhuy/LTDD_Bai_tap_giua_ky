package com.example.baitap.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class Category(
    val id: String,
    val name: String,
    val images: String,
    val description: String,
    val display_order: Int
)

data class Food(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val price: Int?,
    val category: String
)

data class User(
    val username: String,
    val fullName: String,
    val avatar: String?
)
data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)

data class LoginResponse(val success: Boolean, val user: User?, val message: String?)

interface ApiService {
    @GET("api_categories.php")
    suspend fun getCategories(): List<Category>

    @GET("api_foods_by_category.php")
    suspend fun getFoods(@Query("category") category: String): List<Food>

    @POST("api_login.php")
    suspend fun login(@Body request: Map<String, String>): LoginResponse
    @POST("api_register.php")
    suspend fun register(@Body request: Map<String, String>): RegisterResponse
}