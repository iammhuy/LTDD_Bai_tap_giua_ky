// MainHomeScreen.kt
package com.example.baitap.ui.screen
import com.example.baitap.data.Food
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.baitap.R
import com.example.baitap.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(viewModel: MainViewModel) {
    val categories = viewModel.categories
    val foods = viewModel.foods
    val selectedCategory = viewModel.selectedCategory.value
    val userName = viewModel.userName.value
    val isLoading = viewModel.isLoading.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
    ) {
        // Header cam + avatar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF5722))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Hi! $userName", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Eat and Order", color = Color.White.copy(0.9f), fontSize = 14.sp)
                }
                Image(
                    painter = painterResource(R.drawable.avatar_default),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )
            }
        }

        // Search bar
        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
        }

        // Banner Miễn Ship
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color(0xFFFFE0B2))
            ) {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(R.drawable.ic_ship), "Miễn ship", Modifier.size(100.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Miễn Ship", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        Text("14/2 - 28/2", fontSize = 16.sp, color = Color(0xFFE65100))
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(Color(0xFFE65100))) {
                            Text("Đặt Hàng", color = Color.White)
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        // Categories
        item {
            Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(12.dp))
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { item -> // Sử dụng key để tối ưu hóa
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { viewModel.loadFoods(item.name) }
                    ) {
                        AsyncImage(
                            model = item.images,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(
                                    3.dp,
                                    if (selectedCategory == item.name) Color(0xFFFF5722) else Color.White,
                                    CircleShape
                                ) // Thêm hiệu ứng selected
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(item.name, fontSize = 14.sp, fontWeight = if (selectedCategory == item.name) FontWeight.Bold else FontWeight.Medium) // Thêm hiệu ứng selected
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        // Tiêu đề món ăn
        item {
            Text(
                text = "$selectedCategory: ${foods.size}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFFE65100)
            )
            Spacer(Modifier.height(12.dp))
        }

        // GRID MÓN ĂN
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFE65100))
                }
            }
        } else {
            val chunkedFoods = foods.chunked(2)
            items(chunkedFoods) { foodPair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FoodItem(
                        food = foodPair[0],
                        modifier = Modifier.weight(1f)
                    )

                    if (foodPair.size > 1) {
                        FoodItem(
                            food = foodPair[1],
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }

        item { Spacer(Modifier.height(100.dp)) }
    }
}

@Composable
fun FoodItem(food: Food, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            AsyncImage(
                model = food.strMealThumb,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = food.strMeal,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                minLines = 2,
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 15.sp
            )
            Text(
                text = "${food.price ?: 0}đ",
                color = Color(0xFFE65100),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 17.sp
            )
        }
    }
}
