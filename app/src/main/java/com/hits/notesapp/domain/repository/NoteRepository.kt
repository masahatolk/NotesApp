package com.hits.notesapp.domain.repository

import com.hits.notesapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
}