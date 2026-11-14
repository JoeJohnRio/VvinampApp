package com.example.domain.di

import com.example.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    // Album
    singleOf(::GetAlbum)
    singleOf(::AlbumUseCase)

    // Artist
    singleOf(::GetArtistScreenDataUseCase)
    singleOf(::ArtistUseCase)

    // Auth
    singleOf(::LoginUseCase)
    singleOf(::RegisterUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::GetCurrentUserDetailUseCase)
    singleOf(::AuthUseCases)

    // History
    singleOf(::GetListRecentHistoryUseCase)
    singleOf(::HistoryUseCase)

    // Playback
    singleOf(::PlaybackInfoUseCase)
    singleOf(::GetPlayerQueueUseCase)
    singleOf(::AddToQueueUseCase)
    singleOf(::SkipToNextUseCase)
    singleOf(::SkipToPreviousUseCase)
    singleOf(::ClearQueueUseCase)
    singleOf(::PlayContextUseCase)
    singleOf(::PlayYoutubeUseCase)
    singleOf(::SaveCurrentTrackUseCase)
    singleOf(::GetCurrentTrackFlowUseCase)
    singleOf(::PlaybackUseCase)

    // Playlist
    singleOf(::GetMyPlaylistUseCase)
    singleOf(::GetPlaylistTracksUseCase)
    singleOf(::GetPlaylistUseCase)
    singleOf(::GetRecommendedTracksUseCase)
    singleOf(::AddSongToPlaylistUseCase)
    singleOf(::AddYoutubeTrackToPlaylistUseCase)
    singleOf(::EditPlaylistUseCase)
    singleOf(::UpdatePlaylistUseCase)
    singleOf(::UploadPlaylistCoverUseCase)
    singleOf(::GetMyPlaylistFeedUseCase)
    singleOf(::CreatePlaylistUseCase)
    singleOf(::DeletePlaylistUseCase)
    singleOf(::PlaylistUseCase)
}
