package com.example.coffeebara.ui_modules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeebara.data.DeviceInfo
import com.example.coffeebara.navigation.Routes
import com.example.coffeebara.ui.theme.RoyalBlue
import com.example.coffeebara.ui.theme.SoftBrown

@Composable
fun HomeDeviceItemUi(deviceInfo : DeviceInfo, navController: NavController) {

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                horizontal = 30.dp,
                vertical = 20.dp
            )
    ){
        Column{
            Text(
                text = deviceInfo.location,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                LinearProgressIndicator(
                    progress = (deviceInfo.capacity.toFloat()/100),
                    color = RoyalBlue,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(10.dp)
                        .width(220.dp)
                )
                Text(
                    text = deviceInfo.capacity.toString() + "%",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(start = 10.dp),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable {
                    navController.navigate(Routes.Map.route + "?deviceid=${deviceInfo.deviceId}")
                }
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(5.dp)
            ){
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "지도 표시 이동 바")
                Text(
                    text = "위치보기",
                    fontSize = 15.sp
                )
            }
        }
    }
}
