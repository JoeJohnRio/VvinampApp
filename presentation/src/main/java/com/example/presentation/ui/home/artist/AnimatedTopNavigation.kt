package com.example.presentation.ui.home.artist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.getMiddleColor
import com.example.presentation.R
import com.example.presentation.util.PaddingSizeUtil.TopBarHeight


@Composable
fun AnimatedTopNavigation(
    innerPadding: PaddingValues,
    title: String,
    topBarAlpha: Float,
    drawableId: Int = R.drawable.ic_back_chevron,
    onBackClick: () -> Unit,
    topBarNameAlpha: Float,
    topBarColor: Color
) {
    val surfaceHeight = TopBarHeight + innerPadding.calculateTopPadding()
    Surface(
        color = getMiddleColor(
            firstColor = topBarColor,
            secondColor = MaterialTheme.colorScheme.background
        ).copy(
            alpha = topBarAlpha
        ),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(surfaceHeight)
    ) {
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding()),
            contentAlignment = Alignment.BottomCenter // Aligns the Row to the bottom
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(drawableId),
                    contentDescription = "Back"
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        this.alpha = topBarNameAlpha
                    },
                text = title,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 15.sp
                )
            )
        }
    }
}