package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
data class PlaylistUseCase(
    val getMyPlaylistUseCase: GetMyPlaylistUseCase,
    val getMyPlaylistFeedUseCase: GetMyPlaylistFeedUseCase,
    val createPlaylistUseCase: CreatePlaylistUseCase,
    val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    val getPlaylistUseCase: GetPlaylistUseCase,
    val getRecommendedTracksUseCase: GetRecommendedTracksUseCase,
    val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    val addYoutubeTrackToPlaylistUseCase: AddYoutubeTrackToPlaylistUseCase,
    val editPlaylistUseCase: EditPlaylistUseCase,
    val updatePlaylistUseCase: UpdatePlaylistUseCase,
    val uploadPlaylistCoverUseCase: UploadPlaylistCoverUseCase
)

class GetMyPlaylistUseCase(
    private val repository: PlaylistRepository
) {
    operator fun invoke(): Flow<PagingData<PlaylistMinimal>> {
        return repository.getMyPlaylists(10)
    }
}

class GetPlaylistTracksUseCase(
    private val repository: PlaylistRepository
) {
    operator fun invoke(
        id: String,
        sortBy: PlaylistTrackSortByDomain
    ): Flow<PagingData<TrackDomain>> {
        return repository.getPlaylistTracks(id, 10, sortBy)
    }
}

class GetPlaylistUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(id: String): Result<PlaylistComplete> {
        return repository.getPlaylist(id)
    }
}

class GetRecommendedTracksUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(): Result<List<RecommendedTrackDomain>> {
        return repository.getRecommendedTracks()
    }
}

class AddSongToPlaylistUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: String,
        trackId: String
    ): Result<Boolean> {
        return repository.addSongToPlaylist(
            playlistId = playlistId,
            trackId = trackId
        )
    }
}

class AddYoutubeTrackToPlaylistUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: String,
        youtubeId: String,
        title: String,
        artistName: String,
        thumbnailUrl: String?,
        duration: Int,
        position: Int? = null
    ): Result<Boolean> {
        return repository.addYoutubeTrackToPlaylist(
            playlistId = playlistId,
            youtubeId = youtubeId,
            title = title,
            artistName = artistName,
            thumbnailUrl = thumbnailUrl,
            duration = duration,
            position = position
        )
    }
}

class EditPlaylistUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: String,
        deletedTrackIds: List<String>? = null,
        repositionedSongs: List<Pair<String, Int>>? = null
    ): Result<Boolean> {
        return repository.editPlaylist(
            playlistId = playlistId,
            deletedTrackIds = deletedTrackIds,
            repositionedSongs = repositionedSongs
        )
    }
}

class UpdatePlaylistUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String? = null,
        description: String? = null,
        isPublic: Boolean? = null
    ): Result<Boolean> {
        return repository.updatePlaylist(id, title, description, isPublic)
    }
}

class UploadPlaylistCoverUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: String,
        fileName: String,
        contentType: String,
        content: ByteArray
    ): Result<String> {
        return repository.uploadPlaylistCover(playlistId, fileName, contentType, content)
    }
}

class GetMyPlaylistFeedUseCase(
    private val repository: PlaylistRepository
) {
    operator fun invoke(sortOrder: PlaylistSortOrderDomain): Flow<PagingData<PlaylistMinimal>> {
        return repository.getMyPlaylistFeed(10, sortOrder = sortOrder)
    }
}

class CreatePlaylistUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(title: String): Result<String> {
        return repository.createPlaylist(title = title)
    }
}

