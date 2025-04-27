package com.example.myapplication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.data.mapper.MapperUtils
import com.example.myapplication.domain.model.SavedSudokuGame
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.savedGamesDataStore: DataStore<Preferences> by preferencesDataStore(name = "saved_games")
private const val MAX_SAVED_GAMES = 10

@Singleton
class SavedGameDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    // Clave para almacenar la lista de IDs de juegos guardados
    private val savedGamesListKey = stringPreferencesKey("saved_games_list")

    // Función para guardar un juego
    suspend fun saveGame(game: SavedSudokuGame) {
        // Serializar el juego a JSON
        val gameJson = gson.toJson(game)
        val gameKey = stringPreferencesKey("game_${game.id}")

        context.savedGamesDataStore.edit { preferences ->
            // Guardar el juego
            preferences[gameKey] = gameJson

            // Actualizar la lista de juegos guardados
            val currentSavedGames = getSavedGameIds(preferences)
            val updatedGames = if (currentSavedGames.contains(game.id)) {
                // Si el juego ya existe, mantenemos el mismo orden
                currentSavedGames
            } else {
                // Agregamos el nuevo juego al principio y limitamos el total
                listOf(game.id) + currentSavedGames.take(MAX_SAVED_GAMES - 1)
            }

            preferences[savedGamesListKey] = gson.toJson(updatedGames)
        }
    }

    // Función para obtener un juego guardado por su ID
    suspend fun getGame(gameId: String): SavedSudokuGame? {
        val gameKey = stringPreferencesKey("game_${gameId}")
        val preferences = context.savedGamesDataStore.data.map { it[gameKey] }.firstOrNull()

        return preferences?.let {
            try {
                gson.fromJson(it, SavedSudokuGame::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    // Función para obtener todos los juegos guardados
    fun getAllSavedGames(): Flow<List<SavedSudokuGame>> {
        return context.savedGamesDataStore.data.map { preferences ->
            val gameIds = getSavedGameIds(preferences)

            gameIds.mapNotNull { gameId ->
                val gameKey = stringPreferencesKey("game_${gameId}")
                preferences[gameKey]?.let {
                    try {
                        gson.fromJson(it, SavedSudokuGame::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
            }.sortedByDescending { it.lastPlayedAt }
        }
    }

    // Función para eliminar un juego guardado
    suspend fun deleteGame(gameId: String) {
        val gameKey = stringPreferencesKey("game_${gameId}")

        context.savedGamesDataStore.edit { preferences ->
            // Eliminar el juego
            preferences.remove(gameKey)

            // Actualizar la lista de juegos guardados
            val currentSavedGames = getSavedGameIds(preferences)
            val updatedGames = currentSavedGames.filter { it != gameId }

            preferences[savedGamesListKey] = gson.toJson(updatedGames)
        }
    }

    // Función helper para obtener los IDs de los juegos guardados
    private fun getSavedGameIds(preferences: Preferences): List<String> {
        val savedGamesJson = preferences[savedGamesListKey] ?: return emptyList()
        return try {
            gson.fromJson(savedGamesJson, Array<String>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}