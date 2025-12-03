package com.example.baitap.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitap.R
import com.example.baitap.util.SharedPrefsManager

@Composable
fun IntroScreen(
    onStartClick: () -> Unit,
    isLoggedIn: Boolean
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text("Chào mừng đến với Eat & Order", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(0.7f).height(56.dp)
            ) {
                Text("Bắt đầu", fontSize = 18.sp)
            }
        }
    }
}