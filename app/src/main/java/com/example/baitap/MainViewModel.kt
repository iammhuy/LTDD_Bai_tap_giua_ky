package com.example.baitap.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitap.network.Category
import com.example.baitap.network.Food
import com.example.baitap.network.RetrofitInstance
import kotlinx.coroutines.launch
import com.example.baitap.data.mapper.toDataFood
import com.example.baitap.data.Food as DataFood
class MainViewModel : ViewModel() {
    var categories = mutableStateListOf<Category>()
    var foods = mutableStateListOf<DataFood>()
    var selectedCategory = mutableStateOf("Beef")
    var userName = mutableStateOf("Android")
    var isLoading = mutableStateOf(false)

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                categories.clear()
                categories.addAll(RetrofitInstance.api.getCategories())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading.value = false
        }
    }

    fun loadFoods(category: String) {
        selectedCategory.value = category
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Lấy danh sách network.Food từ API
                val networkFoods = RetrofitInstance.api.getFoods(category)

                // Chuyển đổi sang List<DataFood>
                // 2. Không cần khai báo kiểu dữ liệu ở đây nữa, Kotlin sẽ tự suy luận
                val dataFoods = networkFoods.map { it.toDataFood() }

                // Cập nhật danh sách foods
                foods.clear()
                foods.addAll(dataFoods)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading.value = false
        }
    }


    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(mapOf("username" to username, "password" to password))
                if (response.success && response.user != null) {
                    userName.value = response.user.fullName
                    onResult(true, response.user.fullName)
                } else {
                    onResult(false, response.message ?: "Đăng nhập thất bại")
                }
            } catch (e: Exception) {
                onResult(false, "Lỗi mạng")
            }
        }
    }
    // viewmodel/MainViewModel.kt
    // viewmodel/MainViewModel.kt
    // file: app/src/main/java/com/example/baitap/MainViewModel.kt

    fun register(
        username: String,
        password: String,
        fullName: String,
        phone: String,
        onResult: (Boolean, String) -> Unit // Removed @Composable from here
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.register(
                    mapOf(
                        "username" to username,
                        "password" to password,
                        "fullName" to fullName,
                        "phone" to phone
                    )
                )
                if (response.success && response.user != null) {
                    userName.value = response.user.fullName
                    onResult(true, response.user.fullName)
                } else {
                    // Ensure response.message is not null to avoid NullPointerException
                    onResult(false, response.message)
                }
            } catch (e: Exception) {
                onResult(false, "Lỗi kết nối: ${e.message}")
            }
        }
    }
}