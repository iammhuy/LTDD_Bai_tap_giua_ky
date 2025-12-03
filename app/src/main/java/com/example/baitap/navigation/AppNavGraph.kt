// navigation/AppNavGraph.kt
package com.example.baitap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baitap.ui.screen.*
import com.example.baitap.ui.theme.Screen.OtpScreen
import com.example.baitap.ui.theme.Screen.RegisterScreen
import com.example.baitap.util.SharedPrefsManager
import com.example.baitap.viewmodel.MainViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoggedIn = remember { SharedPrefsManager.isLoggedIn(context) }
    val userName = remember { SharedPrefsManager.getUserName(context) }

    // Cập nhật lại tên user trong ViewModel khi mở app
    LaunchedEffect(userName) {
        if (isLoggedIn) {
            viewModel.userName.value = userName
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.MainHome.route else Screen.Intro.route
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(
                isLoggedIn = isLoggedIn, // <-- THÊM DÒNG NÀY
                onStartClick = {
                    // Sửa lỗi cú pháp nhỏ ở dòng dưới
                    val destination = if (isLoggedIn) Screen.MainHome.route else Screen.Login.route

                    // Điều hướng và xóa IntroScreen khỏi back stack
                    navController.navigate(destination) {
                        popUpTo(Screen.Intro.route) {
                            inclusive = true // Xóa IntroScreen khỏi ngăn xếp
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    SharedPrefsManager.saveLogin(context, viewModel.userName.value)
                    navController.navigate(Screen.MainHome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel, // Thêm viewModel
                onNavigateToOtp = { phone ->
                    navController.navigate(Screen.Otp.createRoute(phone))
                },
                onNavigateToLogin = { // Thêm callback để quay lại màn hình Login
                    navController.popBackStack()
                }
            )
        }


        composable(Screen.Otp.route) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(
                phone = phone,
                onVerifySuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MainHome.route) {
            // Đây chính là màn hình chính cũ của bạn
            MainHomeScreen(viewModel = viewModel)
        }
    }
}