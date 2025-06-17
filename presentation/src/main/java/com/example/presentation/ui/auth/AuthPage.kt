package com.example.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R

@Composable
fun AuthPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(104.dp),
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_vvinamp_white),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Millions of songs.\nFree on Vvinamp.",
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

    }
}

@Preview
@Composable
private fun AuthPagePreview() {
    AuthPage()
}