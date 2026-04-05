package com.hits.notesapp.presentation.notes.list

import androidx.lifecycle.ViewModel
import com.hits.notesapp.domain.usecase.GetNotesUseCase

class NotesViewModel(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    val notes = getNotesUseCase()
}