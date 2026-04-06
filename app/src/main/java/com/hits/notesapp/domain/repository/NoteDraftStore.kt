package com.hits.notesapp.domain.repository

import com.hits.notesapp.domain.model.NoteDraft
import kotlinx.coroutines.flow.Flow

interface NoteDraftStore {
    fun observeDraft(): Flow<NoteDraft>
    suspend fun saveDraft(draft: NoteDraft)
    suspend fun clearDraft()
}