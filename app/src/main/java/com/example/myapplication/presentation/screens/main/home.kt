package com.example.myapplication.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.SudokuUiState
import com.example.myapplication.presentation.SudokuViewModel

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: SudokuViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPuzzle(difficulty = "easy", size = 9)
    }

    when (val currentState = state) {
        is SudokuUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is SudokuUiState.Success -> {
            Column(
                modifier = modifier  // Usa el modifier proporcionado
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("¡Puzzle cargado!", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                currentState.puzzle.puzzle.forEach { row ->
                    Row(horizontalArrangement = Arrangement.Center) {
                        row.forEach { cell ->
                            Text(
                                text = if (cell == 0) "." else cell.toString(),
                                modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }

        is SudokuUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${currentState.message}")
            }
        }
    }
}