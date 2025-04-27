package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.SudokuResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SudokuApiService {
    @GET("sudokugenerate")
    suspend fun getSudokuPuzzle(
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("difficulty") difficulty: String
    ): SudokuResponse  // Debe devolver SudokuResponse, no SudokuPuzzle
}