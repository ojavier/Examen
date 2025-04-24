package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.SudokuResponse
import javax.inject.Inject

class SudokuRemoteDataSource @Inject constructor(
    private val api: SudokuApiService
) {
    suspend fun getSudoku(difficulty: String, size: Int): SudokuResponse? {
        val response = api.getSudokuPuzzle(difficulty, size)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Error al obtener Sudoku: ${response.code()}")
        }
    }
}
