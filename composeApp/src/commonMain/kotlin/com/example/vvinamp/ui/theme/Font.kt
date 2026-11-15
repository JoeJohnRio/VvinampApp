package com.example.vvinamp.ui.theme
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun circularStdFontFamily() = FontFamily(
    Font(Res.font.circular_std_bold, FontWeight.Normal),
    Font(Res.font.circular_std_light, FontWeight.Light),
    Font(Res.font.circular_std_medium, FontWeight.Medium),
    Font(Res.font.circular_std_black, FontWeight.Bold),
)

@Composable
fun appTypography(colorScheme: ColorScheme) = Typography(
    bodyLarge = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = colorScheme.inversePrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colorScheme.inversePrimary
    ),
    labelMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        color = colorScheme.inversePrimary
    ),
    displayMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = colorScheme.inversePrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colorScheme.inversePrimary
    ),
    titleLarge = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        color = colorScheme.inversePrimary
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
