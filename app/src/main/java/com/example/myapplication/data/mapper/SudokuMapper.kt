package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.SudokuResponse
import com.example.myapplication.domain.model.SudokuPuzzle

object SudokuMapper {
    fun mapToDomain(response: SudokuResponse, difficulty: String, gridSize: Int): SudokuPuzzle {
        // Usar los nombres correctos: puzzle y solution, no puzzleArray y solutionArray
        val puzzleString = response.puzzle.flatten().joinToString("")
        val solutionString = response.solution.flatten().joinToString("")

        return SudokuPuzzle(
            puzzle = puzzleString,
            solution = solutionString,
            difficulty = difficulty,
            gridSize = gridSize
        )
    }
}