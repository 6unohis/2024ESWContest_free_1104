package com.example.coffeebara.data

data class DeviceInfo(
    val deviceId :Long,
    val location : String,
    val latitude : Double,
    val longitude : Double,
    var capacity : Int,
)
