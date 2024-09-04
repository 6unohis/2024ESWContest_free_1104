package com.example.coffeebara.common

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val result: T? = null
)
