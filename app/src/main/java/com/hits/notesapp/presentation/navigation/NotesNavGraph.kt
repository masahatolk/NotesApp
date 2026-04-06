package com.hits.notesapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hits.notesapp.di.AppContainer
import com.hits.notesapp.presentation.notes.edit.NoteEditorScreen
import com.hits.notesapp.presentation.notes.edit.NoteEditorViewModel
import com.hits.notesapp.presentation.notes.edit.NoteEditorViewModelFactory
import com.hits.notesapp.presentation.notes.list.NotesScreen
import com.hits.notesapp.presentation.notes.list.NotesViewModel
import com.hits.notesapp.presentation.notes.list.NotesViewModelFactory

@Composable
fun NotesNavGraph(appContainer: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.List) {
        composable(Routes.List) {
            val vm: NotesViewModel = viewModel(
                factory = NotesViewModelFactory(
                    getNotesUseCase = appContainer.getNotesUseCase,
                    deleteNoteUseCase = appContainer.deleteNoteUseCase
                )
            )

            NotesScreen(
                viewModel = vm,
                onAddClick = { navController.navigate(Routes.Editor) },
                onEditClick = { id -> navController.navigate("${Routes.Editor}/$id") }
            )
        }

        composable(
            route = "${Routes.Editor}/{${Routes.NoteIdArg}}",
            arguments = listOf(navArgument(Routes.NoteIdArg) { type = NavType.IntType })
        ) {
            val vm: NoteEditorViewModel = viewModel(
                factory = NoteEditorViewModelFactory(
                    noteId = it.arguments?.getInt(Routes.NoteIdArg),
                    getNoteByIdUseCase = appContainer.getNoteByIdUseCase,
                    insertNoteUseCase = appContainer.insertNoteUseCase,
                    draftStore = appContainer.draftStore,
                    reminderScheduler = appContainer.reminderScheduler
                )
            )
            NoteEditorScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.Editor) {
            val vm: NoteEditorViewModel = viewModel(
                factory = NoteEditorViewModelFactory(
                    noteId = null,
                    getNoteByIdUseCase = appContainer.getNoteByIdUseCase,
                    insertNoteUseCase = appContainer.insertNoteUseCase,
                    draftStore = appContainer.draftStore,
                    reminderScheduler = appContainer.reminderScheduler
                )
            )
            NoteEditorScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
    }
}