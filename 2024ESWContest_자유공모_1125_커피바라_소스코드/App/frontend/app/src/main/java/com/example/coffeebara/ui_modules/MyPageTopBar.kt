package com.example.coffeebara.ui_modules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeebara.R
import com.example.coffeebara.ui.theme.CoffeebaraTheme

@Composable
fun MyPageTopBar() {
    Column {
        Text(
            text = "프로필 관리",
            fontFamily = FontFamily(Font(R.font.jalnan)),
            fontSize = 30.sp,
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CoffeebaraTheme {
        MyPageTopBar()
    }
}