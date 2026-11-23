@file:JvmName("AndroidViewModelModuleKt")
package com.example.vvinamp.di

import com.example.vvinamp.ui.main.player.AddToPlaylistViewModel
import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.ui.home.HomeViewModel
import com.example.vvinamp.ui.home.album.AlbumViewModel
import com.example.vvinamp.ui.home.artist.ArtistViewModel
import com.example.vvinamp.ui.home.library.LibraryViewModel
import com.example.vvinamp.ui.home.library.createplaylist.CreatePlaylistViewModel
import com.example.vvinamp.ui.home.library.detailplaylist.PlaylistViewModel
import com.example.vvinamp.ui.home.library.detailplaylist.editplaylist.PlaylistEditViewModel
import com.example.vvinamp.ui.main.MainViewModel
import com.example.vvinamp.ui.search.SearchViewModel
import com.example.vvinamp.ui.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlin.jvm.JvmName

val commonViewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AlbumViewModel)
    viewModelOf(::ArtistViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::CreatePlaylistViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::PlaylistEditViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::AddToPlaylistViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
}
