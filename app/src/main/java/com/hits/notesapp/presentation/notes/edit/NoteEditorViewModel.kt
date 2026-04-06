package com.hits.notesapp.presentation.notes.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.usecase.GetNoteByIdUseCase
import com.hits.notesapp.domain.usecase.InsertNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteEditorViewModel(
    private val noteId: Int?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState

    init {
        if (noteId != null) {
            viewModelScope.launch {
                val note = getNoteByIdUseCase(noteId) ?: return@launch
                _uiState.update {
                    note.content?.let { title ->
                        note.title?.let { existingId ->
                            it.copy(
                                title = existingId,
                                content = title,
                                existingId = note.id
                            )
                        }
                    }!!
                }
            }
        }
    }

    fun onTitleChanged(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onContentChanged(value: String) {
        _uiState.update { it.copy(content = value) }
    }

    fun save(onSaved: () -> Unit) {
        val currentState = _uiState.value

        if (currentState.title.isBlank() && currentState.content.isBlank()) {
            onSaved()
            return
        }

        viewModelScope.launch {
            insertNoteUseCase(
                Note(
                    id = currentState.existingId ?: 0,
                    title = currentState.title,
                    content = currentState.content,
                    timestamp = System.currentTimeMillis()
                )
            )
            onSaved()
        }
    }
}

data class NoteEditorUiState(
    val existingId: Int? = null,
    val title: String = "",
    val content: String = ""
)

class NoteEditorViewModelFactory(
    private val noteId: Int?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteEditorViewModel(noteId, getNoteByIdUseCase, insertNoteUseCase) as T
    }
}
