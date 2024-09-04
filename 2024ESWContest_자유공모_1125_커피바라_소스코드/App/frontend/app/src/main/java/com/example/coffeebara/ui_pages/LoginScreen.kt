package com.example.coffeebara.ui_pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeebara.R
import com.example.coffeebara.navigation.LocalNavGraphViewModelStoreOwner
import com.example.coffeebara.navigation.Routes
import com.example.coffeebara.ui.theme.SoftBrown
import com.example.coffeebara.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
//fun LoginPage() {

    val appViewModel: AppViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var userID by remember {
        mutableStateOf("")
    }
    var userPassword by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            },
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.copybara),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 100.dp)
                    .size(130.dp)
            )
            Text(
                text = "손쉽게 버리는 음료",
                fontFamily = FontFamily(Font(R.font.jalnan)),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 10.dp),
                color = Color.Black
            )
            Text(
                text = "Coffeebara",
                fontFamily = FontFamily(Font(R.font.jalnan)),
                fontSize = 50.sp,
//            modifier = Modifier
//                .padding(top = 10.dp),
                color = Color.Black
            )

            // 아이디 입력 필드
            OutlinedTextField(
                value = userID,
                onValueChange = { userID = it },
                label = { Text("아이디") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(104,66,36), // 포커스 되었을 때 라벨 색상
                    unfocusedLabelColor = Color.Gray, // 포커스 되지 않았을 때 라벨 색상
                    focusedBorderColor = Color(104,66,36), // 포커스 되었을 때 테두리 색상
                    unfocusedBorderColor = Color.Gray // 포커스 되지 않았을 때 테두리 색상
                ),
                shape = RoundedCornerShape(size = 50.dp),
                modifier = Modifier
                    .padding(top = 45.dp)
                    .fillMaxWidth(0.7f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            keyboardController?.hide()
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            // 비밀번호 입력 필드
            OutlinedTextField(
                value = userPassword,
                onValueChange = { userPassword = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(0.7f)
                    .focusRequester(focusRequester2)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            keyboardController?.hide()
                        }
                    },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_lock_24),
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(104,66,36), // 포커스 되었을 때 라벨 색상
                    unfocusedLabelColor = Color.Gray, // 포커스 되지 않았을 때 라벨 색상
                    focusedBorderColor = Color(104,66,36), // 포커스 되었을 때 테두리 색상
                    unfocusedBorderColor = Color.Gray // 포커스 되지 않았을 때 테두리 색상
                ),
                shape = RoundedCornerShape(size = 50.dp),
            )

            // 로그인 버튼
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
                    .background(
//                    brush = Brush.horizontalGradient(
//                        listOf(
//                            Color(234, 32, 90), Color(245, 102, 36)
//                        )
//                    ),
                        color =  SoftBrown,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        when {
                            userID.isEmpty() -> {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("아이디를 입력해주세요.")
                                }
                            }

                            userPassword.isEmpty() -> {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("비밀번호를 입력해주세요.")
                                }
                            }

                            else -> {
                                coroutineScope.launch {
                                    appViewModel.login(
                                        userID,
                                        userPassword,
                                        navController,
                                        snackBarHostState
                                    )
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "로그인", color = Color.White, fontSize = 16.sp)
            }
            Text(
                text = "회원가입",
                color = Color.Gray,
                fontSize = 15.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            navController.navigate(Routes.Register.route)
                        }
                    }
            )
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

//@Preview
//@Composable
//private fun Preview() {
//    CoffeebaraTheme {
//        LoginPage()
//    }
//}