package com.example.coffeebara.ui_pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebara.R
import com.example.coffeebara.navigation.Routes
import com.example.coffeebara.ui.theme.CoffeebaraTheme
import com.example.coffeebara.ui.theme.DeepDarkBrown
import com.example.coffeebara.ui.theme.SoftBrown
import com.example.coffeebara.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
//fun SplashScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
//                brush = Brush.horizontalGradient(
//                    listOf(
//                        Color(202, 159, 125), Color(211, 164, 95)
//                    )
//                )
                color = DeepDarkBrown
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.baseline_miscellaneous_services_24),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 240.dp)
                .size(170.dp)
        )
        Text(
            text = "손쉽게 버리는 음료",
            fontFamily = FontFamily(Font(R.font.jalnan)),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(top = 20.dp),
            color = SoftBrown
        )
        Text(
            text = "Coffeebara",
            fontFamily = FontFamily(Font(R.font.jalnan)),
            fontSize = 50.sp,
//            modifier = Modifier
//                .padding(top = 10.dp),
            color = SoftBrown
        )
    }
    LaunchedEffect(key1 = Unit) {
        delay(1500)
        navController.navigate(Routes.Login.route){
            popUpTo(Routes.Splash.route){
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}

//@Preview
//@Composable
//fun Preview() {
//    CoffeebaraTheme {
//        SplashScreen()
//    }
//}