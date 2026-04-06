package com.hits.notesapp.domain.usecase

import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)
}