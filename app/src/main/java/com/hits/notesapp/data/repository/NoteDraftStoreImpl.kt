package com.hits.notesapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hits.notesapp.domain.model.NoteDraft
import com.hits.notesapp.domain.repository.NoteDraftStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class NoteDraftStoreImpl(context: Context) : NoteDraftStore {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("note_draft.preferences_pb") }
    )

    override fun observeDraft(): Flow<NoteDraft> {
        return dataStore.data
            .catch {
                if (it is IOException) emit(emptyPreferences()) else throw it
            }
            .map { prefs ->
                NoteDraft(
                    title = prefs[TITLE] ?: "",
                    content = prefs[CONTENT] ?: "",
                    tags = prefs[TAGS] ?: "",
                    imageUri = prefs[IMAGE_URI] ?: "",
                    reminderAt = prefs[REMINDER_AT]
                )
            }
    }

    override suspend fun saveDraft(draft: NoteDraft) {
        dataStore.edit { prefs ->
            prefs[TITLE] = draft.title
            prefs[CONTENT] = draft.content
            prefs[TAGS] = draft.tags
            prefs[IMAGE_URI] = draft.imageUri
            if (draft.reminderAt == null) {
                prefs.remove(REMINDER_AT)
            } else {
                prefs[REMINDER_AT] = draft.reminderAt
            }
        }
    }

    override suspend fun clearDraft() {
        dataStore.edit { it.clear() }
    }

    private companion object {
        val TITLE = stringPreferencesKey("title")
        val CONTENT = stringPreferencesKey("content")
        val TAGS = stringPreferencesKey("tags")
        val IMAGE_URI = stringPreferencesKey("image_uri")
        val REMINDER_AT = longPreferencesKey("reminder_at")
    }
}