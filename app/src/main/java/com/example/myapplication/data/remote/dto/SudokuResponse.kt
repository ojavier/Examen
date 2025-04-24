package com.example.myapplication.data.remote.dto

data class SudokuResponse(
    val puzzle: List<List<Int>>,
    val solution: List<List<Int>>
)
