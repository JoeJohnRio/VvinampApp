package com.example.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.presentation.R

val Gotham = FontFamily(
    Font(R.font.gotham_regular, FontWeight.Normal),
    Font(R.font.gotham_light, FontWeight.Light),
    Font(R.font.gotham_medium, FontWeight.Medium),
    Font(R.font.gotham_bold, FontWeight.Bold),
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = AppColours.OffWhite
    ),
    bodyMedium = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    labelMedium = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    displayMedium = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    headlineMedium = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    titleLarge = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        color = AppColours.OffWhite
    )
)

@Composable
fun TSBodyMediumWhite(): TextStyle {
    return MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.inversePrimary
    )
}

@Composable
fun TSLabelMediumWhite(): TextStyle {
    return MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.inversePrimary
    )
}

@Composable
fun TSHeadlineMediumWhite(): TextStyle {
    return MaterialTheme.typography.headlineMedium.copy(
        color = MaterialTheme.colorScheme.inversePrimary
    )
}

@Composable
fun TSDisplayMediumWhite(): TextStyle {
    return MaterialTheme.typography.displayMedium.copy(
        color = MaterialTheme.colorScheme.inversePrimary
    )
}