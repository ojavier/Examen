package com.example.myapplication.domain.model

import com.example.myapplication.data.remote.dto.SudokuResponse

data class SudokuPuzzle(
    val puzzle: String,  // Cadena formateada de dígitos
    val solution: String,  // Cadena formateada de dígitos
    val difficulty: String,
    val gridSize: Int
)

// Función de extensión para convertir la respuesta de la API a un modelo de dominio (SudokuPuzzle)
fun SudokuResponse.toDomain(difficulty: String, gridSize: Int): SudokuPuzzle {
    // Convertir los arrays 2D a strings o al formato que necesites
    val puzzleString = puzzle.flatten().joinToString("") { it.toString() }
    val solutionString = solution.flatten().joinToString("") { it.toString() }

    return SudokuPuzzle(
        puzzle = puzzleString,
        solution = solutionString,
        difficulty = difficulty,
        gridSize = gridSize
    )
}
