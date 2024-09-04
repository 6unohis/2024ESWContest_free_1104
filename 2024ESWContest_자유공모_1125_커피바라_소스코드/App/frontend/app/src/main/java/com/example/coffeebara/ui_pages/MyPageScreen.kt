package com.example.coffeebara.ui_pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.coffeebara.R
import com.example.coffeebara.navigation.Routes
import com.example.coffeebara.ui.theme.BottomBar
import com.example.coffeebara.ui.theme.CoffeebaraTheme
import com.example.coffeebara.ui.theme.White
import com.example.coffeebara.ui_modules.BottomNavigationBar
import com.example.coffeebara.ui_modules.MyPageTopBar

@Composable
fun MyPageScreen(navController : NavController) {

//    val appViewModel: AppViewModel =
//        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    Scaffold (
        bottomBar = { BottomNavigationBar(navController) },
        topBar = { MyPageTopBar() }
    ){contentPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth(),
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "프로필 이미지",
                        tint = White,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .size(70.dp)
                            .background(
                                color = BottomBar,
                                shape = CircleShape
                            )
                    )
                    Text(
//                        text = appViewModel.userInfo.component1().name,
                        text = "장시게이",
                        modifier = Modifier
                            .padding(top = 15.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "담당 구역",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "건국대학교",
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "담당 기기",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "10" + "개",
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 40.dp)
                    .background(
                        color = Color(android.graphics.Color.parseColor("#d5cbbd")),
                        shape = RoundedCornerShape(topEnd = 40.dp, topStart = 40.dp)
                    ),
            ){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    items(MyPageOptions.Options){
                        ColCol(it)
                    }
                }
            }
        }
    }
}

@Composable
fun ColCol(myPageOption: MyPageOption) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 40.dp)
            .clickable {

            }
    ){
        Icon(
            imageVector = myPageOption.selectIcon,
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
        )
        Text(
            text = myPageOption.title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 5.dp)
        )
    }
}

data class MyPageOption(
    val title: String,
    val selectIcon: ImageVector,
    val onSelectedIcon: ImageVector,
    val route: String,
)

object MyPageOptions {
    val Options = listOf(
        MyPageOption(
            title = "기기 추가",
            selectIcon = Icons.Default.Add,
            onSelectedIcon = Icons.Outlined.Add,
            route = ""
        ),
        MyPageOption(
            title = "내 담당 기기",
            selectIcon = Icons.Default.Build,
            onSelectedIcon = Icons.Outlined.Build,
            route = Routes.Home.route
        ),
        MyPageOption(
            title = "프로필 수정",
            selectIcon = Icons.Default.Person,
            onSelectedIcon = Icons.Outlined.Person,
            route = Routes.MyPage.route
        ),
        MyPageOption(
            title = "프로필 수정",
            selectIcon = Icons.Default.Person,
            onSelectedIcon = Icons.Outlined.Person,
            route = Routes.MyPage.route
        ),
        MyPageOption(
            title = "프로필 수정",
            selectIcon = Icons.Default.Person,
            onSelectedIcon = Icons.Outlined.Person,
            route = Routes.MyPage.route
        ),
        MyPageOption(
            title = "프로필 수정",
            selectIcon = Icons.Default.Person,
            onSelectedIcon = Icons.Outlined.Person,
            route = Routes.MyPage.route
        ),
        MyPageOption(
            title = "프로필 수정",
            selectIcon = Icons.Default.Person,
            onSelectedIcon = Icons.Outlined.Person,
            route = Routes.MyPage.route
        ),
        MyPageOption(
            title = "프로필 수정",
            selectIcon = Icons.Default.Person,
            onSelectedIcon = Icons.Outlined.Person,
            route = Routes.MyPage.route
        ),
    )
}

@Preview
@Composable
private fun Preview() {
    val navController = rememberNavController()
    CoffeebaraTheme {
        MyPageScreen(navController)
    }
}