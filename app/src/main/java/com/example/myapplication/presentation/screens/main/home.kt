package com.example.myapplication.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.SudokuUiState
import com.example.myapplication.presentation.SudokuViewModel
import com.example.myapplication.presentation.screens.main.components.DropdownMenuDifficulty
import com.example.myapplication.presentation.screens.main.components.DropdownMenuSize

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: SudokuViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // NUEVOS STATES PARA OPCIONES
    var selectedDifficulty by remember { mutableStateOf("easy") }
    var selectedSize by remember { mutableStateOf(9) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // MENÚ DE DIFICULTAD
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dificultad: ", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.width(8.dp))
            DropdownMenuDifficulty(selectedDifficulty) { selectedDifficulty = it }
        }

        Spacer(Modifier.height(8.dp))

        // MENÚ DE TAMAÑO
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tamaño: ", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.width(8.dp))
            DropdownMenuSize(selectedSize) { selectedSize = it }
        }

        Spacer(Modifier.height(16.dp))

        // BOTÓN PARA GENERAR NUEVO PUZZLE
        Button(
            onClick = {
                viewModel.loadPuzzle(difficulty = selectedDifficulty, size = selectedSize)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Generar Puzzle")
        }

        Spacer(Modifier.height(16.dp))

        // CONTENIDO SEGÚN ESTADO
        when (val currentState = state) {
            is SudokuUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SudokuUiState.Success -> {
                val puzzleString = currentState.puzzle.puzzle

                if (!puzzleString.isNullOrEmpty()) {
                    val gridSize = Math.sqrt(puzzleString.length.toDouble()).toInt()
                    val cellStates = remember(puzzleString) {
                        mutableStateListOf<String>().apply {
                            puzzleString.forEach { char ->
                                if (char != '0' && char != '.') {
                                    add(char.toString()) // valor fijo del Sudoku
                                } else {
                                    add("") // celda vacía
                                }
                            }
                        }
                    }


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridSize),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(cellStates.size) { index ->
                            val originalValue = puzzleString[index]
                            val userInput = cellStates[index]

                            Card(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(2.dp),
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    if (originalValue != '0' && originalValue != '.') {
                                        // Número original, bloqueado
                                        Text(
                                            text = originalValue.toString(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    } else {
                                        // Celda editable
                                        TextField(
                                            value = userInput,
                                            onValueChange = { newValue ->
                                                // Solo permitir un número entre 1-9
                                                if (newValue.length <= 1 && (newValue.isEmpty() || newValue[0] in '1'..'9')) {
                                                    cellStates[index] = newValue
                                                }
                                            },
                                            singleLine = true,
                                            textStyle = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(2.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text("El puzzle está vacío o en formato incorrecto")
                }
            }

            is SudokuUiState.Error -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${currentState.message}")
                }
            }
        }
    }
}