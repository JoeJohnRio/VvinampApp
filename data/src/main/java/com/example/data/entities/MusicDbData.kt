package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entity.MusicEntity

@Entity(tableName = "movies")
data class MusicDbData(
    @PrimaryKey val id: Int,
    val description: String,
    val image: String,
    val backgroundUrl: String,
    val title: String,
    val category: String,
)

fun MusicDbData.toDomain() = MusicEntity(
    id = id,
    title = title,
    description = description,
    image = image,
    category = category,
    backgroundUrl = backgroundUrl
)