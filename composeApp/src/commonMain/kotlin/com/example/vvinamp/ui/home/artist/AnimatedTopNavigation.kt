package com.example.vvinamp.ui.home.artist
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.util.getMiddleColor

import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.util.PaddingSizeUtil.TopBarHeight
import org.jetbrains.compose.resources.DrawableResource


@Composable
fun AnimatedTopNavigation(
    innerPadding: PaddingValues,
    title: String,
    topBarAlpha: Float,
    imageVector: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
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
                    imageVector = imageVector,
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
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun AnimatedTopSlidingNameNavigation(
    innerPadding: PaddingValues,
    title: String,
    topBarAlpha: Float,
    imageVector: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    onBackClick: () -> Unit,
    titleProgress: Float,
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
                .clipToBounds() // CRITICAL: This hides the text when it gets pushed down
                .padding(top = innerPadding.calculateTopPadding()),
            contentAlignment = Alignment.BottomCenter
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Back"
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        val hiddenOffset = 40.dp.toPx()
                        this.translationY = hiddenOffset * (1f - titleProgress)
                    },
                text = title,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 18.sp
                )
            )
        }
    }
}