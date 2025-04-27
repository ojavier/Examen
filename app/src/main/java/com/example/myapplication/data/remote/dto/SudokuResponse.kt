package com.example.myapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SudokuResponse(
    @SerializedName("puzzle")
    val puzzle: List<List<Int>>,  // Lista de listas de enteros

    @SerializedName("solution")
    val solution: List<List<Int>>  // Lista de listas de enteros
)
