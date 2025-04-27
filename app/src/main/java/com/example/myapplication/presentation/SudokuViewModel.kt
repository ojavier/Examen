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

    // Nuevo estado para el input del usuario
    private val _userInput = MutableStateFlow<Map<Int, String>?>(null)
    val userInput: StateFlow<Map<Int, String>?> = _userInput

    // Nuevo estado para controlar si se completó
    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete

    // Nuevo estado para controlar si hay solución incorrecta
    private val _isIncorrect = MutableStateFlow(false)
    val isIncorrect: StateFlow<Boolean> = _isIncorrect

    fun loadPuzzle(difficulty: String = "medium", size: Int = 9) {
        _state.value = SudokuUiState.Loading
        _userInput.value = null
        _isComplete.value = false
        _isIncorrect.value = false
        viewModelScope.launch {
            try {
                val puzzle = getSudokuPuzzleUseCase(
                    gridSize = size,
                    difficulty = difficulty
                )
                _state.value = SudokuUiState.Success(puzzle)
                // Inicializar userInput con celdas vacías para las editables
                initUserInput(puzzle.puzzle)
            } catch (e: Exception) {
                _state.value = SudokuUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private fun initUserInput(puzzleString: String) {
        val inputMap = mutableMapOf<Int, String>()
        puzzleString.forEachIndexed { index, char ->
            if (char == '0' || char == '.') {
                inputMap[index] = ""
            }
        }
        _userInput.value = inputMap
    }

    fun updateCell(index: Int, value: String) {
        val currentInput = _userInput.value?.toMutableMap() ?: return
        currentInput[index] = value
        _userInput.value = currentInput

        // Reset el estado de incorrecto cuando el usuario empieza a modificar de nuevo
        if (_isIncorrect.value) {
            _isIncorrect.value = false
        }

        checkCompletion()
    }

    private fun checkCompletion() {
        val currentState = _state.value
        val currentInput = _userInput.value

        if (currentState is SudokuUiState.Success && currentInput != null) {
            val solution = currentState.puzzle.solution

            // Verifica si todas las celdas están llenas
            val allFilled = currentInput.all { (_, value) -> value.isNotEmpty() }

            if (allFilled) {
                // Verifica si todas las celdas son correctas
                val isCorrect = currentInput.all { (index, value) ->
                    value.isNotEmpty() && value[0] == solution[index]
                }

                if (isCorrect) {
                    _isComplete.value = true
                } else {
                    _isIncorrect.value = true
                }
            }
        }
    }

    fun resetIncorrectState() {
        _isIncorrect.value = false
    }
}