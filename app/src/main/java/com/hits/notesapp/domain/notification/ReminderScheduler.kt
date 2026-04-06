package com.hits.notesapp.domain.notification

import com.hits.notesapp.domain.model.Note

interface ReminderScheduler {
    fun schedule(note: Note)
}