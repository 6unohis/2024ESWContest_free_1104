package com.example.coffeebara.ui_pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeebara.navigation.LocalNavGraphViewModelStoreOwner
import com.example.coffeebara.ui.theme.SoftBrown
import com.example.coffeebara.ui_modules.BottomNavigationBar
import com.example.coffeebara.ui_modules.HomeDeviceItemUi
import com.example.coffeebara.ui_modules.HomeTopBar
import com.example.coffeebara.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(navController: NavController) {

    val appViewModel: AppViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val coroutineScope = rememberCoroutineScope()

    if(appViewModel.deviceList.isEmpty()){
        coroutineScope.launch {
            appViewModel.loadDeviceInfo()
        }
    }

    Scaffold (
        bottomBar = { BottomNavigationBar(navController = navController) },
        topBar = { HomeTopBar() }
    ){
        contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ){
            LazyColumn {
                items(appViewModel.deviceList){ 
                    HomeDeviceItemUi(deviceInfo = it,navController)
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp),
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