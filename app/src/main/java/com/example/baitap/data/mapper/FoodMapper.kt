package com.example.baitap.data.mapper

import com.example.baitap.data.Food as DataFood
import com.example.baitap.network.Food as NetworkFood

// Sửa kiểu dữ liệu trả về từ NetworkFood thành DataFood
fun NetworkFood.toDataFood(): DataFood {
    return DataFood(
        idMeal = this.idMeal,
        strMeal = this.strMeal,
        strMealThumb = this.strMealThumb,
        // Giá được tạo ngẫu nhiên hoặc gán mặc định nếu API không trả về
        price = this.price ?: (15..100).random() * 1000
    )
}
