package com.example.vvinamp.ui.theme
import vvinampapp.composeapp.generated.resources.*

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
fun appTypography() = Typography(
    bodyLarge = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = AppColours.OffWhite
    ),
    bodyMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    labelMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    displayMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    headlineMedium = TextStyle(
        fontFamily = circularStdFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = AppColours.OffWhite
    ),
    titleLarge = TextStyle(
        fontFamily = circularStdFontFamily(),
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
