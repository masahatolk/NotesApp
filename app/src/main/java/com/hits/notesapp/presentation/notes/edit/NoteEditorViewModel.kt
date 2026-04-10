package com.hits.notesapp.presentation.notes.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.model.NoteDraft
import com.hits.notesapp.domain.notification.ReminderScheduler
import com.hits.notesapp.domain.repository.NoteDraftStore
import com.hits.notesapp.domain.usecase.GetNoteByIdUseCase
import com.hits.notesapp.domain.usecase.InsertNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteEditorViewModel(
    private val noteId: Int?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val draftStore: NoteDraftStore,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState

    init {
        viewModelScope.launch {
            if (noteId != null) {
                val note = getNoteByIdUseCase(noteId) ?: return@launch
                _uiState.update {
                    it.copy(
                        title = note.title.orEmpty(),
                        content = note.content.orEmpty(),
                        tags = note.tags.joinToString(", "),
                        imageUri = note.imageUri.orEmpty(),
                        reminderAt = note.reminderAt,
                        existingId = note.id
                    )
                }
            } else {
                draftStore.observeDraft().collect { draft ->
                    _uiState.update {
                        it.copy(
                            title = draft.title,
                            content = draft.content,
                            tags = draft.tags,
                            imageUri = draft.imageUri,
                            reminderAt = draft.reminderAt
                        )
                    }
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

    fun onTagsChanged(value: String) {
        _uiState.update { it.copy(tags = value) }
    }

    fun onImageChanged(value: String) {
        _uiState.update { it.copy(imageUri = value) }
    }

    fun setReminder(hourFromNow: Int?) {
        _uiState.update {
            it.copy(reminderAt = hourFromNow?.let { hours -> System.currentTimeMillis() + hours * 60L * 60L * 1000L })
        }
    }

    fun setReminderAt(timestamp: Long?) {
        _uiState.update { it.copy(reminderAt = timestamp) }
    }

    fun onExitWithoutSave(onBack: () -> Unit) {
        viewModelScope.launch {
            if (noteId == null) {
                val current = _uiState.value
                draftStore.saveDraft(
                    NoteDraft(
                        title = current.title,
                        content = current.content,
                        tags = current.tags,
                        imageUri = current.imageUri,
                        reminderAt = current.reminderAt
                    )
                )
            }
            onBack()
        }
    }

    fun save(onSaved: () -> Unit) {
        val currentState = _uiState.value

        if (currentState.title.isBlank() && currentState.content.isBlank() && currentState.imageUri.isBlank()) {
            onSaved()
            return
        }

        viewModelScope.launch {
            val note = Note(
                id = currentState.existingId ?: 0,
                title = currentState.title,
                content = currentState.content,
                tags = currentState.tags.split(",").map { it.trim() }.filter { it.isNotBlank() },
                imageUri = currentState.imageUri.ifBlank { null },
                reminderAt = currentState.reminderAt,
                timestamp = System.currentTimeMillis()
            )

            insertNoteUseCase(note)
            if (noteId == null) {
                draftStore.clearDraft()
            }
            reminderScheduler.schedule(note)
            onSaved()
        }
    }
}

data class NoteEditorUiState(
    val existingId: Int? = null,
    val title: String = "",
    val content: String = "",
    val tags: String = "",
    val imageUri: String = "",
    val reminderAt: Long? = null
)

class NoteEditorViewModelFactory(
    private val noteId: Int?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val draftStore: NoteDraftStore,
    private val reminderScheduler: ReminderScheduler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteEditorViewModel(
            noteId = noteId,
            getNoteByIdUseCase = getNoteByIdUseCase,
            insertNoteUseCase = insertNoteUseCase,
            draftStore = draftStore,
            reminderScheduler = reminderScheduler
        ) as T
    }
}
