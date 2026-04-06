package com.hits.notesapp.domain.usecase

import com.hits.notesapp.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteById(id)
}