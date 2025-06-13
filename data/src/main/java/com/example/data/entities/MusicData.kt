package com.example.data.entities

import com.example.domain.entity.MusicEntity
import com.google.gson.annotations.SerializedName

data class MusicData(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("backgroundUrl") val backgroundUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
)

fun MusicData.toDomain() = MusicEntity(
    id = id,
    image = image,
    backgroundUrl = backgroundUrl,
    description = description,
    title = title,
    category = category
)

fun MusicData.toDbData() = MusicDbData(
    id = id,
    image = image,
    description = description,
    title = title,
    category = category,
    backgroundUrl = backgroundUrl
)