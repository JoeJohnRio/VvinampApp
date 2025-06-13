package com.example.data.repository.music

import androidx.paging.PagingSource
import com.example.data.entities.MusicData
import com.example.data.entities.MusicDbData
import com.example.data.entities.MusicRemoteKeyDbData
import com.example.domain.entity.MusicEntity
import com.example.domain.util.Result


interface MusicDataSource {

    interface Remote {
        suspend fun getMovies(page: Int, limit: Int): Result<List<MusicData>>
    }

    interface Local {
        suspend fun getMovies(): Result<List<MusicEntity>>
        suspend fun getLastRemoteKey(): MusicRemoteKeyDbData?
                fun movies(): PagingSource<Int, MusicDbData>
        //        suspend fun getMovie(movieId: Int): Result<MovieEntity>
        suspend fun saveMovies(movies: List<MusicData>)
        suspend fun saveRemoteKey(key: MusicRemoteKeyDbData)
        suspend fun clearMovies()
        suspend fun clearRemoteKeys()
    }
}