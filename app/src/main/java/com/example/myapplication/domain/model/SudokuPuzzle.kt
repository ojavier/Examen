package com.example.myapplication.domain.model

data class SudokuPuzzle(
    val puzzle: List<List<Int>>,
    val solution: List<List<Int>>
)