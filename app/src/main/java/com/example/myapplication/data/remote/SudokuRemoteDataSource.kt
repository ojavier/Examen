package com.example.myapplication.data.remote

import android.util.Log
import com.example.myapplication.data.remote.dto.SudokuResponse
import com.example.myapplication.domain.model.SudokuPuzzle
import retrofit2.HttpException
import javax.inject.Inject
import com.google.gson.Gson

class SudokuRemoteDataSource @Inject constructor(
    private val api: SudokuApiService
) {
    suspend fun getSudoku(difficulty: String, boxSize: Int): SudokuPuzzle? {
        try {
            val gridSize = boxSize * boxSize
            val response = api.getSudokuPuzzle(boxSize, boxSize, difficulty)

            Log.d("SudokuResponse", "Raw Response: $response")

            // Verifica si tenemos datos válidos
            if (response.puzzle == null || response.solution == null) {
                Log.e("SudokuResponse", "Puzzle o Solution es null")
                return null
            }

            // Convertir las listas de listas en strings de dígitos
            val puzzleString = processNestedListToString(response.puzzle)
            val solutionString = processNestedListToString(response.solution)

            Log.d("SudokuResponse", "Processed Puzzle: $puzzleString")
            Log.d("SudokuResponse", "Processed Solution: $solutionString")

            return SudokuPuzzle(
                puzzle = puzzleString,
                solution = solutionString,
                difficulty = difficulty,
                gridSize = gridSize
            )
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("SudokuResponse", "HTTP Error: ${e.code()} ${e.message()} - $errorBody")
            throw Exception("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Log.e("SudokuResponse", "Exception: ${e.message}", e)
            throw Exception("Error al obtener el Sudoku: ${e.message}")
        }
    }

    // Función para convertir la lista anidada a string
    private fun processNestedListToString(nestedList: List<List<Int>>): String {
        return nestedList.flatten().joinToString("")
    }
}