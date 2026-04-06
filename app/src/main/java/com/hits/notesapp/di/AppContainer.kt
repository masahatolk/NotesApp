package com.hits.notesapp.di

import android.content.Context
import com.hits.notesapp.data.local.NotesDatabase
import com.hits.notesapp.data.repository.NoteRepositoryImpl
import com.hits.notesapp.domain.repository.NoteRepository
import com.hits.notesapp.domain.usecase.DeleteNoteUseCase
import com.hits.notesapp.domain.usecase.GetNoteByIdUseCase
import com.hits.notesapp.domain.usecase.GetNotesUseCase
import com.hits.notesapp.domain.usecase.InsertNoteUseCase

class AppContainer(context: Context) {

    private val database = NotesDatabase.create(context)
    private val noteRepository: NoteRepository = NoteRepositoryImpl(database.noteDao())

    val getNotesUseCase = GetNotesUseCase(noteRepository)
    val getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository)
    val insertNoteUseCase = InsertNoteUseCase(noteRepository)
    val deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
}