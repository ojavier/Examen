package com.example.myapplication.domain.model

data class SudokuResponse(
    val puzzle: List<List<Int>>,
    val solution: List<List<Int>>
)
