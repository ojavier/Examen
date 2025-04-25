package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.SudokuPuzzle
import com.example.myapplication.domain.usecase.GetSudokuPuzzleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SudokuUiState {
    object Loading : SudokuUiState()
    data class Success(val puzzle: SudokuPuzzle) : SudokuUiState()
    data class Error(val message: String) : SudokuUiState()
}

@HiltViewModel
class SudokuViewModel @Inject constructor(
    private val getSudokuPuzzleUseCase: GetSudokuPuzzleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SudokuUiState>(SudokuUiState.Loading)
    val state: StateFlow<SudokuUiState> = _state

    fun loadPuzzle(difficulty: String = "medium", size: Int = 9) {
        _state.value = SudokuUiState.Loading
        viewModelScope.launch {
            try {
                val puzzle = getSudokuPuzzleUseCase(difficulty, size)
                _state.value = SudokuUiState.Success(puzzle)
            } catch (e: Exception) {
                _state.value = SudokuUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
