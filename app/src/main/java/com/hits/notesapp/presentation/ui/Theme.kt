package com.hits.notesapp.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val Typography = _root_ide_package_.androidx.compose.material3.Typography()

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun NotesAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}