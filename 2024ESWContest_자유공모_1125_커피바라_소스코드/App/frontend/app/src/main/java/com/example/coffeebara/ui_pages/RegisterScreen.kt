package com.example.coffeebara.ui_pages

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coffeebara.R
import com.example.coffeebara.navigation.LocalNavGraphViewModelStoreOwner
import com.example.coffeebara.ui.theme.SoftBrown
import com.example.coffeebara.ui.theme.White
import com.example.coffeebara.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val appViewModel: AppViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val focusRequester = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var userID by remember { mutableStateOf("") }
    var userPasswd by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

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
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.copybara),
                contentDescription = "Store Icon",
                tint = Color.Black,
                modifier = Modifier
                    .padding(top = 100.dp)
                    .size(100.dp)
            )

            Text(
                text = "손쉽게 버리는 음료",
                fontFamily = FontFamily(Font(R.font.jalnan)),
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 0.dp)
            )

            Text(
                text = "Coffeebara",
                fontFamily = FontFamily(Font(R.font.jalnan)),
                fontSize = 50.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 0.dp)
            )

            // 이름 입력 필드
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_drive_file_rename_outline_24),
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
                    .focusRequester(focusRequester3)
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
                    .padding(top = 0.dp)
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
                value = userPasswd,
                onValueChange = { userPasswd = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(vertical = 0.dp)
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

            // 비밀번호 재확인 입력 필드
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("비밀번호 확인") },
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
                    .padding(vertical = 0.dp)
                    .fillMaxWidth(0.7f)
                    .focusRequester(focusRequester4)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            keyboardController?.hide()
                        }
                    },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_24),
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

            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
                    .background(
//                        brush = Brush.horizontalGradient(
//                            listOf(
//                                Color(234, 32, 90), Color(245, 102, 36)
//                            )
//                        ),
                        color = SoftBrown,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        when {
                            name.isEmpty() || userID.isEmpty() || userPasswd.isEmpty()-> {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("빈칸을 입력해주세요.")
                                }
                            }

                            userPasswd != confirmPassword -> {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("비밀번호가 일치하지 않습니다.")
                                }
                            }

                            else -> {
                                coroutineScope.launch {
                                    appViewModel.createUser(
                                        userID,
                                        userPasswd,
                                        name,
                                        snackBarHostState,
                                        navController
                                    )
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "회원가입", color = White, fontSize = 16.sp)
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}