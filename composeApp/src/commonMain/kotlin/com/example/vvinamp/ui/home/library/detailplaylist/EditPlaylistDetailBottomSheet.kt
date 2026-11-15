package com.example.vvinamp.ui.home.library.detailplaylist
import com.example.vvinamp.util.rememberImagePickerLauncher
import com.example.vvinamp.util.showToast
import vvinampapp.composeapp.generated.resources.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import com.example.vvinamp.ui.theme.PhoneColumnMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

import com.example.vvinamp.ui.home.library.widget.image.PlaylistCover
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.widget.TextFieldNormal
import com.example.vvinamp.util.PaddingSizeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistDetailBottomSheet(
    uiState: PlaylistUiState,
    onEvent: (PlaylistUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val launcher = rememberImagePickerLauncher { uriString ->
        onEvent(PlaylistUiEvent.onCoverImageChanged(uriString))
    }

    val isChanged = uiState.editName != (uiState.playlist?.title ?: "") || 
                    uiState.editDescription != (uiState.playlist?.description ?: "") ||
                    uiState.editCoverUri != null

    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.isEditDetailSheetVisible) {
            showToast("Update failed: ${uiState.error}")
        }
    }

    ModalBottomSheet(
        sheetMaxWidth = PhoneColumnMaxWidth,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Name & details",
                    style = TSHeadlineMediumWhite().copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancel",
                        style = TSDisplayMediumWhite().copy(fontSize = 14.sp),
                        modifier = Modifier.clickable { onEvent(PlaylistUiEvent.onCancelEditDetail) }
                    )

                    Text(
                        text = if (uiState.isSaving) "Saving..." else "Save",
                        style = TSDisplayMediumWhite().copy(
                            fontSize = 14.sp,
                            color = if (isChanged && !uiState.isSaving) MaterialTheme.colorScheme.primary else Color.Gray
                        ),
                        modifier = Modifier.clickable(enabled = isChanged && !uiState.isSaving) {
                            onEvent(PlaylistUiEvent.onSaveEditDetail)
                        }
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier.size(PaddingSizeUtil.PlaylistEditPhotoSize)) {
                    if (uiState.editCoverUri != null) {
                        AsyncImage(
                            model = uiState.editCoverUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(PaddingSizeUtil.PlaylistEditPhotoSize)
                        )
                    } else {
                        PlaylistCover(
                            coverImage = uiState.playlist?.coverImage.orEmpty(),
                            covers = uiState.playlist?.albumCovers ?: emptyList(),
                            modifier = Modifier.size(PaddingSizeUtil.PlaylistEditPhotoSize)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .clickable {
                                launcher.launch()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit cover",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    TextFieldNormal(
                        text = uiState.editName,
                        onValueChanged = { onEvent(PlaylistUiEvent.onEditNameChanged(it)) },
                        placeholder = "Playlist name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    TextFieldNormal(
                        text = uiState.editDescription,
                        onValueChanged = { onEvent(PlaylistUiEvent.onEditDescriptionChanged(it)) },
                        placeholder = "Add a description",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Action Items
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = if (uiState.playlist?.isPublic == false) "Make public" else "Make private",
                    style = TSDisplayMediumWhite().copy(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEvent(PlaylistUiEvent.onMakePrivate) }
                )

                Text(
                    text = "Delete playlist",
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEvent(PlaylistUiEvent.onDeletePlaylist) }
                )
            }
        }
    }
}
