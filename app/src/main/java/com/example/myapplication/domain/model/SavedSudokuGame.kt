package com.example.myapplication.domain.model

import java.util.Date

/**
 * Representa una partida de Sudoku guardada con toda la información necesaria
 * para restaurar el estado de juego cuando el usuario quiera continuar.
 */
data class SavedSudokuGame(
    val id: String, // Identificador único para la partida guardada
    val puzzle: SudokuPuzzle, // La estructura del puzzle de Sudoku
    val currentGrid: List<List<Int>>, // Estado actual del tablero con los números ingresados por el usuario
    val notes: List<List<Set<Int>>>, // Notas que el usuario ha colocado en las celdas
    val elapsedTimeSeconds: Long, // Tiempo transcurrido en segundos
    val difficulty: String, // Dificultad del puzzle
    val createdAt: Date, // Fecha de creación de la partida guardada
    val lastPlayedAt: Date, // Última vez que se jugó
    val isCompleted: Boolean = false // Indica si el puzzle está completado
) {
    // Método para verificar si la partida está en progreso (no completada)
    fun isInProgress(): Boolean = !isCompleted

    // Método para calcular el porcentaje de completado
    fun completionPercentage(): Float {
        val totalCells = puzzle.gridSize * puzzle.gridSize
        val filledCells = currentGrid.flatten().count { it != 0 }
        return filledCells.toFloat() / totalCells * 100
    }

    // Método para crear una copia actualizada con el tiempo actual
    fun withUpdatedTime(additionalSeconds: Long): SavedSudokuGame {
        return this.copy(
            elapsedTimeSeconds = this.elapsedTimeSeconds + additionalSeconds,
            lastPlayedAt = Date()
        )
    }

    // Método para actualizar el estado actual del tablero
    fun withUpdatedGrid(newGrid: List<List<Int>>, isComplete: Boolean = false): SavedSudokuGame {
        return this.copy(
            currentGrid = newGrid,
            lastPlayedAt = Date(),
            isCompleted = isComplete
        )
    }
}