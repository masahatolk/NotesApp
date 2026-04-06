package com.hits.notesapp.presentation.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.usecase.DeleteNoteUseCase
import com.hits.notesapp.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val tagFilter = MutableStateFlow("")

    val uiState: StateFlow<NotesUiState> = combine(
        getNotesUseCase(),
        searchQuery,
        tagFilter
    ) { notes, query, selectedTag ->
        val filteredByQuery = if (query.isBlank()) {
            notes
        } else {
            notes.filter { it.title?.contains(query, ignoreCase = true) ?: false }
        }

        val filtered = if (selectedTag.isBlank()) {
            filteredByQuery
        } else {
            filteredByQuery.filter { note ->
                note.tags.any { tag -> tag.equals(selectedTag, ignoreCase = true) }
            }
        }

        val allTags = notes.flatMap { it.tags }.distinct().sorted()

        NotesUiState(
            query = query,
            selectedTag = selectedTag,
            availableTags = allTags,
            notes = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NotesUiState()
    )

    fun onSearchChanged(value: String) {
        searchQuery.update { value }
    }

    fun onTagFilterChanged(value: String) {
        tagFilter.update { value }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            deleteNoteUseCase(id)
        }
    }
}

data class NotesUiState(
    val query: String = "",
    val selectedTag: String = "",
    val availableTags: List<String> = emptyList(),
    val notes: List<Note> = emptyList()
)

class NotesViewModelFactory(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(getNotesUseCase, deleteNoteUseCase) as T
    }
}