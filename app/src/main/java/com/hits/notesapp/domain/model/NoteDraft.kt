package com.hits.notesapp.domain.model

data class NoteDraft(
    val title: String = "",
    val content: String = "",
    val tags: String = "",
    val imageUri: String = "",
    val reminderAt: Long? = null
)