package com.example.coffeebara.repository

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import com.example.coffeebara.data.DeviceInfo
import com.example.coffeebara.data.dto.request.CreateUserRequest
import com.example.coffeebara.data.dto.request.LoginUserRequest
import com.example.coffeebara.data.dto.response.LoadDeviceInfo
import com.example.coffeebara.retrofit.RetrofitClient
import com.google.gson.Gson

sealed class NetworkResult<out T>{
    data class Success<out T>(val data:T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}

data class ErrorResponse(val message: String)
data class MessageOfCreateUser(
    val userId : Long,
    val isSuccess : Boolean
)
data class BooleanOfLoginUser(
    val isLogin : Boolean,
    val userId : Long?,
)

class MainRepository {

    suspend fun createUserResponse(createUserRequest: CreateUserRequest, snackbarHostState: SnackbarHostState) : MessageOfCreateUser? {
        return try{
            val response = RetrofitClient.getAPIService.createUser(createUserRequest)
            if(response.isSuccessful){
                val body = response.body()
                if(body != null){
                    val result = body.result
                    snackbarHostState.showSnackbar(body.message)
                    result?.let { MessageOfCreateUser(it.userId, true) }
                }
                else{
                    Log.d("createUserResponse", "body가 본문에 없습니다.")
                    null
                }
            } else {
                Log.d("createUserResponse", "HTTP 요청이 실패하였습니다. 코드: ${response.code()}, 메시지: ${response.message()}")
                val errorBodyString = response.errorBody()?.string()
                val errorMessage = if (!errorBodyString.isNullOrBlank()) {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBodyString, ErrorResponse::class.java)
                    errorResponse.message
                } else {
                    "서버에서 에러 메시지를 가져올 수 없습니다."
                }
                snackbarHostState.showSnackbar(errorMessage)
                MessageOfCreateUser(0L, false)
            }
        }catch(e: Exception){
            Log.e("createUserResponse" , e.toString())
            null
        }
    }

    suspend fun loginUserResponse(loginUserRequest: LoginUserRequest, snackbarHostState: SnackbarHostState) : BooleanOfLoginUser? {
        return try{
            val response = RetrofitClient.getAPIService.loginUser(loginUserRequest)

            if(response.isSuccessful){
                BooleanOfLoginUser(true, response.body()?.result?.userId)
            }
            else{
                Log.d("createUserResponse", "HTTP 요청이 실패하였습니다. 코드: ${response.code()}, 메시지: ${response.message()}")
                val errorBodyString = response.errorBody()?.string()
                val errorMessage = if (!errorBodyString.isNullOrBlank()) {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBodyString, ErrorResponse::class.java)
                    errorResponse.message
                } else {
                    "서버에서 에러 메시지를 가져올 수 없습니다."
                }
                snackbarHostState.showSnackbar(errorMessage)
                BooleanOfLoginUser(false, null)
            }
        }catch(e: Exception){
            Log.e("loginUserResponse" , e.toString())
            null
        }
    }

    suspend fun loadDeviceInfo() : List<DeviceInfo>?{
        return try{
            val response = RetrofitClient.getAPIService.loadDeviceInfo()

            if(response.isSuccessful){
                response.body()?.result?.deviceInfos
            }
            else{
                Log.d("createUserResponse", "HTTP 요청이 실패하였습니다. 코드: ${response.code()}, 메시지: ${response.message()}")
                null
            }
        }catch(e: Exception){
            Log.e("loginUserResponse" , e.toString())
            null
        }
    }

}