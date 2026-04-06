package com.hits.notesapp.presentation.notes.edit

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.onImageChanged(uri?.toString().orEmpty())
    }

    BackHandler {
        viewModel.onExitWithoutSave(onBack)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(if (state.existingId == null) "Новая заметка" else "Редактирование")
            }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = viewModel::onTitleChanged,
                label = { Text("Заголовок") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.tags,
                onValueChange = viewModel::onTagsChanged,
                label = { Text("Теги через запятую") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = state.content,
                onValueChange = viewModel::onContentChanged,
                label = { Text("Тело заметки") }
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    Button(onClick = { imagePicker.launch("image/*") }) {
                        Text("Добавить изображение")
                    }
                }
                item {
                    Button(onClick = { viewModel.setReminder(1) }) {
                        Text("Напомнить через 1 час")
                    }
                }
                item {
                    Button(onClick = { viewModel.setReminder(24) }) {
                        Text("Напомнить завтра")
                    }
                }
                item {
                    Button(onClick = { viewModel.setReminder(null) }) {
                        Text("Убрать напоминание")
                    }
                }
            }

            if (state.reminderAt != null) {
                Text(
                    text = "Напоминание: ${state.reminderAt?.asReadableDate()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (state.imageUri.isNotBlank()) {
                AsyncImage(
                    model = state.imageUri,
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier.size(120.dp)
                )
            }

            Button(
                onClick = { viewModel.save(onBack) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

private fun Long.asReadableDate(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(this))
}