package com.hits.notesapp.domain.model

data class Note(
    val id: Int,
    val title: String?,
    val content: String?,
    val timestamp: Long
)