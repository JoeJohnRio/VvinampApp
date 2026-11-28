package com.example.vvinamp.util

import androidx.compose.ui.unit.dp

object PaddingSizeUtil {

    val ArtistImagePadding = 300.dp
    val AlbumImageSize = 252.dp

    val PlaylistNoPhotoSize = 160.dp
    // The playlist page cover matches the album page cover.
    val PlaylistCoverSize = AlbumImageSize
    val LibraryItemSize = 64.dp
    val PlaylistEditPhotoSize = 120.dp
    val ListeningHistoryPhotoSize = 56.dp

    val NormalPadding = 16.dp

    val BigPadding = 32.dp

    val TopBarHeight = 56.dp

    val AlbumHeroSize = AlbumImageSize + BigPadding + 46.dp
    val PlaylistHeroSize = PlaylistCoverSize + BigPadding + 46.dp
}
