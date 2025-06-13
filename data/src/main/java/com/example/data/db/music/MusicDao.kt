package com.example.data.db.music

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entities.MusicDbData

@Dao
interface MusicDao {

    @Query("SELECT * FROM movies ORDER BY category")
    fun movies(): PagingSource<Int, MusicDbData>

    /**
     * Get all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM movies ORDER BY category")
    fun getMovies(): List<MusicDbData>

    /**
     * Get movie by id.
     * **/
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovie(movieId: Int): MusicDbData?

    /**
     * Insert all movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<MusicDbData>)

    /**
     * Delete all movies except favorites.
     */
    @Query("DELETE FROM movies")
    suspend fun clearMoviesExceptFavorites()
}