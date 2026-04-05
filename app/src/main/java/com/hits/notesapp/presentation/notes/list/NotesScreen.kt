package com.hits.notesapp.presentation.notes.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    onAddClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) {
        if (notes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет заметок")
            }
        } else {
            LazyColumn {
                items(notes) { note ->
                    Text(note.title ?: "Без названия")
                }
            }
        }
    }
}