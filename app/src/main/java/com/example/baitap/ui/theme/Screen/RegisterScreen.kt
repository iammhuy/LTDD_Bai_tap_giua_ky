package com.example.baitap.ui.theme.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.baitap.viewmodel.MainViewModel
import com.example.baitap.util.SharedPrefsManager

// ui/screen/RegisterScreen.kt
@Composable
fun RegisterScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToOtp: (String) -> Unit, // <-- THAY ĐỔI: Thêm tham số này
    onNavigateToLogin: () -> Unit // <-- THÊM: Callback để quay lại màn hình Login
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Đăng ký tài khoản", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tên đăng nhập") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank() || phone.isBlank()) {
                    error = "Vui lòng điền đầy đủ thông tin"
                    return@Button
                }
                isLoading = true
                viewModel.register(username, password, fullName.ifBlank { username }, phone) { success, msg ->
                    isLoading = false
                    if (success) {
                        // 2. Sử dụng biến 'context' đã lấy ở trên
                        SharedPrefsManager.saveLogin(context, fullName.ifBlank { username })
                        // <-- THAY ĐỔI: Gọi callback mới để chuyển sang màn hình OTP
                        onNavigateToOtp(phone)
                    } else {
                        error = msg
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            else Text("Đăng ký ngay")
        }

        Spacer(modifier = Modifier.height(16.dp))
        // <-- THÊM: Gọi callback để quay lại màn hình Login khi nhấn nút
        TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Đã có tài khoản? Đăng nhập")
        }
    }
}

@Composable
fun OtpScreen(phone: String, onVerifySuccess: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Nhập mã OTP gửi đến $phone")
        // Bạn thêm 6 ô nhập số ở đây (hoặc dùng thư viện)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onVerifySuccess, modifier = Modifier.fillMaxWidth()) {
            Text("Xác nhận")
        }
    }
}
