package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.SudokuResponse
import com.example.myapplication.domain.model.SudokuPuzzle

// Convierte la respuesta de la API a un modelo de dominio
fun SudokuResponse.toDomain(difficulty: String, gridSize: Int): SudokuPuzzle {
    // Convierte el array 2D de puzzle a una representación en string
    val puzzleString = buildString {
        puzzle.forEach { row ->
            row.forEach { cell ->
                append(if (cell == 0) "." else cell.toString())
            }
        }
    }

    // Convierte el array 2D de solución a una representación en string
    val solutionString = buildString {
        solution.forEach { row ->
            row.forEach { cell ->
                append(cell.toString())
            }
        }
    }

    return SudokuPuzzle(
        puzzle = puzzleString,
        solution = solutionString,
        difficulty = difficulty,
        gridSize = gridSize
    )
}