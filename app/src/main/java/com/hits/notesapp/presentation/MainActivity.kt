package com.hits.notesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hits.notesapp.di.AppContainer
import com.hits.notesapp.presentation.navigation.NotesNavGraph
import com.hits.notesapp.presentation.ui.NotesAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = AppContainer(applicationContext)

        setContent {
            NotesAppTheme {
                NotesNavGraph(appContainer = appContainer)
            }
        }
    }
}