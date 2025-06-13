package com.example.data.repository.music

import androidx.paging.PagingSource
import com.example.data.db.music.MusicDao
import com.example.data.db.music.MusicRemoteKeyDao
import com.example.data.entities.MusicData
import com.example.data.entities.MusicDbData
import com.example.data.entities.MusicRemoteKeyDbData
import com.example.data.entities.toDbData
import com.example.data.entities.toDomain
import com.example.data.exception.DataNotAvailableException
import com.example.domain.entity.MusicEntity
import com.example.domain.util.Result


class MusicLocalDataSource(
    private val musicDao: MusicDao,
    private val remoteKeyDao: MusicRemoteKeyDao,
) : MusicDataSource.Local {

    override fun movies(): PagingSource<Int, MusicDbData> = musicDao.movies()

    override suspend fun getMovies(): Result<List<MusicEntity>> {
        val music = musicDao.getMovies()
        return if (music.isNotEmpty()) {
            Result.Success(music.map { it.toDomain() })
        } else {
            Result.Error(DataNotAvailableException())
        }
    }

//    override suspend fun getMovie(movieId: Int): Result<MusicEntity> {
//        return musicDao.getMovie(movieId)?.let {
//            Result.Success(it.toDomain())
//        } ?: Result.Error(DataNotAvailableException())
//    }

    override suspend fun saveMovies(movies: List<MusicData>) {
        musicDao.saveMovies(movies.map { it.toDbData() })
    }

    override suspend fun getLastRemoteKey(): MusicRemoteKeyDbData? {
        return remoteKeyDao.getLastRemoteKey()
    }

    override suspend fun saveRemoteKey(key: MusicRemoteKeyDbData) {
        remoteKeyDao.saveRemoteKey(key)
    }

    override suspend fun clearMovies() {
        musicDao.clearMoviesExceptFavorites()
    }

    override suspend fun clearRemoteKeys() {
        remoteKeyDao.clearRemoteKeys()
    }
}