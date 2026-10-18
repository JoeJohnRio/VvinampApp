package com.example.vvinamp.ui.widget.playback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.vvinamp.ui.theme.AppColours

@Composable
fun DownloadProgressDialog(
    progress: Int,
    trackTitle: String,
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AppColours.SurfaceContainerHighBlack)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Downloading Track",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AppColours.White
                    ),
                    textAlign = TextAlign.Center
                )

                if (trackTitle.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = trackTitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AppColours.Outline
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(24.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(96.dp)
                ) {
                    val coercedProgress = progress.coerceIn(0, 100) / 100f
                    CircularProgressIndicator(
                        progress = { coercedProgress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 6.dp,
                        color = AppColours.PrimaryGreen,
                        trackColor = AppColours.Secondary
                    )

                    Text(
                        text = "$progress%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = AppColours.White
                        )
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = if (progress >= 100) "Starting playback..." else "Downloading from YouTube...",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AppColours.Outline
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
