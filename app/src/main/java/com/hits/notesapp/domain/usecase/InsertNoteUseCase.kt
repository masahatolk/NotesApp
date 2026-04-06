package com.hits.notesapp.domain.usecase

import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.repository.NoteRepository

class InsertNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.insertNote(note)
}