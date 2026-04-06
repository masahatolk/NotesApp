package com.hits.notesapp.domain.model

data class Note(
    val id: Int,
    val title: String?,
    val content: String?,
    val tags: List<String> = emptyList(),
    val imageUri: String? = null,
    val reminderAt: Long? = null,
    val timestamp: Long
)