package com.example.myapplication.presentation.screens.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun DropdownMenuDifficulty(selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("easy", "medium", "hard")

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { difficulty ->
                DropdownMenuItem(
                    text = { Text(difficulty) },
                    onClick = {
                        onSelected(difficulty)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownMenuSize(selected: Int, onSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(4, 9)

    Box {
        Button(onClick = { expanded = true }) {
            Text("${selected}x${selected}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { size ->
                DropdownMenuItem(
                    text = { Text("${size}x${size}") },
                    onClick = {
                        onSelected(size)
                        expanded = false
                    }
                )
            }
        }
    }
}
