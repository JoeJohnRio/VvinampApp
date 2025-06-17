package com.example.presentation.mapper

import com.example.domain.entity.MusicEntity
import com.example.presentation.entities.MusicListItem


fun MusicEntity.toPresentation() = MusicListItem.Music(
    id = id,
    imageUrl = image,
    category = category
)

fun MusicEntity.toMovieListItem(): MusicListItem = this.toPresentation()