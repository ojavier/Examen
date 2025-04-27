package com.example.myapplication.domain.usecase

import com.example.myapplication.data.local.SavedGameDataStore
import com.example.myapplication.domain.model.SavedSudokuGame
import com.example.myapplication.domain.model.SudokuPuzzle
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class SaveLoadGameUseCase @Inject constructor(
    private val savedGameDataStore: SavedGameDataStore
) {
    // Guardar la partida actual
    suspend fun saveGame(
        puzzle: SudokuPuzzle,
        currentGrid: List<List<Int>>,
        notes: List<List<Set<Int>>>,
        elapsedTimeSeconds: Long,
        difficulty: String,
        gameId: String? = null // Opcional, para actualizar una partida existente
    ): String {
        // Crear un nuevo ID si no se proporciona uno
        val id = gameId ?: UUID.randomUUID().toString()

        val savedGame = SavedSudokuGame(
            id = id,
            puzzle = puzzle,
            currentGrid = currentGrid,
            notes = notes,
            elapsedTimeSeconds = elapsedTimeSeconds,
            difficulty = difficulty,
            createdAt = Date(),
            lastPlayedAt = Date(),
            isCompleted = false
        )

        savedGameDataStore.saveGame(savedGame)
        return id
    }

    // Actualizar una partida ya guardada
    suspend fun updateSavedGame(
        gameId: String,
        currentGrid: List<List<Int>>,
        notes: List<List<Set<Int>>>,
        elapsedTimeSeconds: Long,
        isCompleted: Boolean = false
    ) {
        val existingGame = savedGameDataStore.getGame(gameId) ?: return

        val updatedGame = existingGame.copy(
            currentGrid = currentGrid,
            notes = notes,
            elapsedTimeSeconds = elapsedTimeSeconds,
            lastPlayedAt = Date(),
            isCompleted = isCompleted
        )

        savedGameDataStore.saveGame(updatedGame)
    }

    // Cargar una partida guardada por su ID
    suspend fun loadGame(gameId: String): SavedSudokuGame? {
        return savedGameDataStore.getGame(gameId)
    }

    // Obtener todas las partidas guardadas
    fun getAllSavedGames(): Flow<List<SavedSudokuGame>> {
        return savedGameDataStore.getAllSavedGames()
    }

    // Eliminar una partida guardada
    suspend fun deleteGame(gameId: String) {
        savedGameDataStore.deleteGame(gameId)
    }

    // Marcar una partida como completada
    suspend fun markGameAsCompleted(gameId: String) {
        val game = savedGameDataStore.getGame(gameId) ?: return
        savedGameDataStore.saveGame(game.copy(isCompleted = true, lastPlayedAt = Date()))
    }
}