package com.hits.notesapp.data.repository

import com.hits.notesapp.data.local.NoteDao
import com.hits.notesapp.data.local.NoteEntity
import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)?.toDomain()
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}

fun NoteEntity.toDomain() = Note(id, title, content, timestamp)

fun Note.toEntity() = NoteEntity(id, title, content, timestamp)