package com.hits.notesapp.domain.usecase

import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes()
    }
}