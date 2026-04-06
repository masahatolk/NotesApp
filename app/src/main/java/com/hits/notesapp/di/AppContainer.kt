package com.hits.notesapp.di

import android.content.Context
import com.hits.notesapp.data.local.NotesDatabase
import com.hits.notesapp.data.repository.NoteDraftStoreImpl
import com.hits.notesapp.data.repository.NoteRepositoryImpl
import com.hits.notesapp.data.notification.AlarmReminderScheduler
import com.hits.notesapp.domain.notification.ReminderScheduler
import com.hits.notesapp.domain.repository.NoteDraftStore
import com.hits.notesapp.domain.repository.NoteRepository
import com.hits.notesapp.domain.usecase.DeleteNoteUseCase
import com.hits.notesapp.domain.usecase.GetNoteByIdUseCase
import com.hits.notesapp.domain.usecase.GetNotesUseCase
import com.hits.notesapp.domain.usecase.InsertNoteUseCase

class AppContainer(context: Context) {

    private val database = NotesDatabase.create(context)
    private val noteRepository: NoteRepository = NoteRepositoryImpl(database.noteDao())

    val draftStore: NoteDraftStore = NoteDraftStoreImpl(context)
    val reminderScheduler: ReminderScheduler = AlarmReminderScheduler(context)

    val getNotesUseCase = GetNotesUseCase(noteRepository)
    val getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository)
    val insertNoteUseCase = InsertNoteUseCase(noteRepository)
    val deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
}