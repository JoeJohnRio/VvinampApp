package com.example.presentation.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    darkMode: Boolean,
    fontFamily: FontFamily = FontFamily.Cursive,
    fontSize: TextUnit = 25.sp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    onThemeUpdated: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = fontSize,
                    fontFamily = fontFamily,
                    color = colors.primaryContainer,
                    fontWeight = fontWeight
                )
            },
            actions = {
                IconButton(
                    onClick = { onSearchClick() }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(
                    onClick = { onThemeUpdated() }
                ) {
                    val icon = if (darkMode) {
                        Filled.DarkMode
                    } else {
                        Outlined.DarkMode
                    }
                    Icon(imageVector = icon, contentDescription = "Dark Mode")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(apiLevel = 33)
fun TopBarCustom(
    title: String = stringResource(R.string.title_create_account),
    style: TextStyle = TSBodyMediumWhite(),
    icon: Painter? = painterResource(R.drawable.ic_back_arrow),
    onBackClick: () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
        containerColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = style
            )
        },
        colors = colors,
        navigationIcon = {
            icon?.let {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onBackClick()
                        }
                        .padding(start = 12.dp),
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = null
                )
            }
        },
    )
}