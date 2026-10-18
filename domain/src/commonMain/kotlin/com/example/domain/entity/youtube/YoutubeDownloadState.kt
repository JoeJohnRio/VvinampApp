package com.example.domain.entity.youtube

import com.example.domain.entity.playbackinfo.PlaybackInfoDomain

sealed interface YoutubeDownloadState {
    data class Progress(val progress: Int) : YoutubeDownloadState
    data class Completed(val playbackInfo: PlaybackInfoDomain) : YoutubeDownloadState
    data class Error(val message: String) : YoutubeDownloadState
}
