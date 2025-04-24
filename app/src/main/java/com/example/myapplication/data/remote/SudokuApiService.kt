package com.example.myapplication.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.myapplication.data.remote.dto.SudokuResponse

interface SudokuApiService {

    @GET("sudoku")
    suspend fun getSudokuPuzzle(
        @Query("difficulty") difficulty: String = "easy",
        @Query("size") size: Int = 9
    ): Response<SudokuResponse>
}
