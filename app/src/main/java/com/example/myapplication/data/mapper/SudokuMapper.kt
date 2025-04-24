package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.SudokuResponse
import com.example.myapplication.domain.model.SudokuPuzzle

fun SudokuResponse.toDomain(): SudokuPuzzle {
    return SudokuPuzzle(
        puzzle = this.puzzle,
        solution = this.solution
    )
}