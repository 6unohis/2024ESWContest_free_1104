package com.example.coffeebara.data

data class UserInfo(
    val userId:Long,
    val name :String,
    val latitude: Double?,
    val longitude: Double?,
    val role :String?
)
