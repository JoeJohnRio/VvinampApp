package com.example.data.api

import com.example.data.entities.MusicData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApi {
    @GET("/movies?&_sort=category,id")
    suspend fun getMovies(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<MusicData>

    @GET("/movies")
    suspend fun getMovies(@Query("id") movieIds: List<Int>): List<MusicData>

    @GET("/movies/{id}")
    suspend fun getMovie(@Path("id") movieId: Int): MusicData

    @GET("/movies")
    suspend fun search(
        @Query("q") query: String,
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<MusicData>
}