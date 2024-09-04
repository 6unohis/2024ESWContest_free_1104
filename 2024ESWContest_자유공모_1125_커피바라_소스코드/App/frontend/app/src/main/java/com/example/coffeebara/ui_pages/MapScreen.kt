package com.example.coffeebara.ui_pages

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeebara.navigation.LocalNavGraphViewModelStoreOwner
import com.example.coffeebara.ui.theme.DarkBrown
import com.example.coffeebara.ui.theme.SoftBrown
import com.example.coffeebara.ui_modules.BottomNavigationBar
import com.example.coffeebara.viewmodel.AppViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun MainScreen(navController: NavController, deviceId: Long?) {

    val appViewModel: AppViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        bottomBar = { BottomNavigationBar(navController) }
    ){contentPadding ->

        var staringPoint =  LatLng(37.541631, 127.076575)
        if(deviceId?.toInt() != 0) {
            for(device in appViewModel.deviceList){
                if(device.deviceId == deviceId){
                    staringPoint = LatLng(device.latitude, device.longitude)
                    break
                }
            }
        }

        val cameraPosition = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(staringPoint,16f)
        }

        var searchKeyword by remember {
            mutableStateOf("")
        }

        Box(
            modifier = Modifier.padding(contentPadding)
        ){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
            ){
                for(device in appViewModel.deviceList){
                    Marker(
                        state = MarkerState(position = LatLng(device.latitude, device.longitude)),
                        title = device.location,
                        snippet = device.capacity.toString() + "%",
                    )
                }
            }

            TextField(
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
                    //.background(SoftBrown, RoundedCornerShape(30.dp))
                    .alpha(0.8f)
                    .border(2.dp, DarkBrown, RoundedCornerShape(30.dp)),
                value = searchKeyword,
                onValueChange = { text -> searchKeyword = text },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "돋보기"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                label = { Text(text = "검색하기")}
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 100.dp),
                    //.alpha(0.8f),
                onClick = {
                    coroutineScope.launch {
                        appViewModel.loadDeviceInfo()
                    }
                },
                containerColor = SoftBrown
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "새로 고침")
            }
        }
    }
}