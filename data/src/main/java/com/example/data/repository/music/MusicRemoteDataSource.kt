package com.example.data.repository.music

import com.example.data.api.MusicApi
import com.example.data.entities.MusicData
import com.example.data.util.safeApiCall
import com.example.domain.util.Result

class MusicRemoteDataSource(
    private val musicApi: MusicApi
) : MusicDataSource.Remote {

    override suspend fun getMovies(page: Int, limit: Int): Result<List<MusicData>> = safeApiCall {
        musicApi.getMovies(page, limit)
    }

//    override suspend fun getMovies(movieIds: List<Int>): Result<List<MusicData>> = safeApiCall {
//        musicApi.getMovies(movieIds)
//    }
//
//    override suspend fun getMovie(movieId: Int): Result<MusicData> = safeApiCall {
//        musicApi.getMovie(movieId)
//    }
//
//    override suspend fun search(query: String, page: Int, limit: Int): Result<List<MusicData>> = safeApiCall {
//        musicApi.search(query, page, limit)
//    }
}
