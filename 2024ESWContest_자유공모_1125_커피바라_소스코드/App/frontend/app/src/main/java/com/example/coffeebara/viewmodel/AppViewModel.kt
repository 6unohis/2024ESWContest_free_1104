package com.example.coffeebara.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.coffeebara.data.DeviceInfo
import com.example.coffeebara.data.UserInfo
import com.example.coffeebara.data.dto.request.CreateUserRequest
import com.example.coffeebara.data.dto.request.LoginUserRequest
import com.example.coffeebara.repository.MainRepository
import com.example.coffeebara.repository.MessageOfCreateUser
import kotlinx.coroutines.async

class AppViewModel :ViewModel() {

    val repository : MainRepository = MainRepository()

    var deviceList = mutableListOf<DeviceInfo>(
//        DeviceInfo(1, "청심대", 37.542344, 127.076906, 30),
//        DeviceInfo(2, "공학관 A동", 37.541943, 127.078804, 70),
//        DeviceInfo(3, "새천년관", 37.543513, 127.077337, 10),
//        DeviceInfo(4, "경영관", 37.544397, 127.076345, 40),
//        DeviceInfo(5, "산학협동관", 37.539745, 127.073139, 50),
//        DeviceInfo(6, "예술디자인관", 37.542861, 127.073062, 80),
//        DeviceInfo(7, "도서관 자료실", 37.542064, 127.073570, 20),
//        DeviceInfo(8, "도서관 열람실", 37.542064, 127.074118, 95),
//        DeviceInfo(9, "수의대", 37.539199, 127.074609, 5),
//        DeviceInfo(10, "과학관", 37.541581, 127.080337, 100),
    )

    var userInfo = mutableStateOf<UserInfo>(
        UserInfo(1,"장시게이", 37.54, 127.07, "manager")
    )

    private var userId : Long? = null

    suspend fun createUser(id : String, password : String, name: String, snackBarHostState: SnackbarHostState, navController: NavController){
        val createUserRequest = CreateUserRequest(
            id = id,
            password = password,
            name = name,
            latitude = 37.541631,
            longitude = 127.076575
        )

        return viewModelScope.async {
            val result = repository.createUserResponse(createUserRequest, snackBarHostState)
            userId = result?.userId
            if (result != null) {
                if(result.isSuccess) {
                    navController.navigate("Login")
                }
            }
        }.await()
    }

    suspend fun login(id: String, userPassword: String, navController:NavController, snackBarHostState: SnackbarHostState){
        val loginUserRequest = LoginUserRequest(
            id,
            userPassword
        )

        viewModelScope.async {
            val result = repository.loginUserResponse(loginUserRequest, snackBarHostState)
            userId = result?.userId
            if (result != null) {
                if(result.isLogin) {
                    navController.navigate("Home")
                }
            }
        }.await()
    }

    suspend fun loadDeviceInfo(){
        viewModelScope.async {
            val result = repository.loadDeviceInfo()

            deviceList.clear()

            result?.forEach { deviceInfo ->
                deviceList.add(deviceInfo)
            }

        }.await()
    }
}