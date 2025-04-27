package com.example.myapplication.domain.usecase

import android.util.Log
import com.example.myapplication.data.remote.SudokuRemoteDataSource
import com.example.myapplication.domain.model.SudokuPuzzle
import javax.inject.Inject

class GetSudokuPuzzleUseCase @Inject constructor(
    private val remoteDataSource: SudokuRemoteDataSource
) {

    suspend operator fun invoke(
        gridSize: Int = 9,
        difficulty: String = "easy"
    ): SudokuPuzzle {
        // Calcular el tamaño de cada caja para la API
        val boxSize = when (gridSize) {
            4 -> 2  // Para un Sudoku 4x4, cada caja es 2x2
            9 -> 3  // Para un Sudoku 9x9, cada caja es 3x3
            16 -> 4 // Para un Sudoku 16x16, cada caja es 4x4
            else -> throw IllegalArgumentException("Tamaño de grid no válido. Debe ser 4, 9 o 16")
        }

        Log.d("API_REQUEST", "Requesting Sudoku with difficulty=$difficulty, boxSize=$boxSize")

        return remoteDataSource.getSudoku(difficulty, boxSize)
            ?: throw Exception("No se pudo obtener un Sudoku válido")
    }
}