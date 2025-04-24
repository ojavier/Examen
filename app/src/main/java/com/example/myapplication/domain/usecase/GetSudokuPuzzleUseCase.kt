package com.example.myapplication.domain.usecase

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.remote.SudokuRemoteDataSource
import com.example.myapplication.domain.model.SudokuPuzzle
import javax.inject.Inject

class GetSudokuPuzzleUseCase @Inject constructor(
    private val remoteDataSource: SudokuRemoteDataSource
) {
    suspend operator fun invoke(difficulty: String = "medium", size: Int = 9): SudokuPuzzle {
        val response = remoteDataSource.getSudoku(difficulty, size)
            ?: throw IllegalStateException("SudokuResponse fue null")

        return response.toDomain()
    }
}
