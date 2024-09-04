package com.example.coffeebara.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.coffeebara.ui_pages.HomeScreen
import com.example.coffeebara.ui_pages.LoginScreen
import com.example.coffeebara.ui_pages.MainScreen
import com.example.coffeebara.ui_pages.MyPageScreen
import com.example.coffeebara.ui_pages.RegisterScreen
import com.example.coffeebara.ui_pages.SplashScreen

sealed class Routes(val route:String){
    object Splash:Routes("Splash")
    object Login:Routes("Login")
    object Register:Routes("Register")
    object Map:Routes("Map")
    object MyPage:Routes("MyPage")
    object Home:Routes("Home")
}

//ViewModel 을 LocalContextOf를 활용해서 navigation 으로 넘기기 위한 전역 함수 1개와 전역 변수 1개
@Composable
fun rememberViewModelStoreOwner():ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

val LocalNavGraphViewModelStoreOwner = staticCompositionLocalOf<ViewModelStoreOwner>{
    error("Undefined")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(navController : NavHostController) {

    val navStoreOwner = rememberViewModelStoreOwner()

    CompositionLocalProvider(LocalNavGraphViewModelStoreOwner provides navStoreOwner) {
        NavHost(navController = navController, startDestination = Routes.Splash.route){
            composable(route = Routes.Splash.route) {
                SplashScreen(navController = navController)
            }
            composable(route = Routes.Map.route + "?deviceid={deviceId}",
                arguments = listOf(
                    navArgument(name = "deviceId")
                    {
                        type = NavType.LongType }
                )) {
                val deviceId = it.arguments?.getLong("deviceId")
                MainScreen(navController = navController, deviceId = deviceId)
            }
            composable(route = Routes.MyPage.route) {
                MyPageScreen(navController = navController)
            }
            composable(route = Routes.Login.route) {
                LoginScreen(navController)
            }
            composable(route = Routes.Register.route) {
                RegisterScreen(navController)
            }
            composable(route = Routes.Home.route) {
                HomeScreen(navController)
            }
        }
    }
}