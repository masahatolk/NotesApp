package com.hits.notesapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hits.notesapp.data.notification.ReminderReceiver
import com.hits.notesapp.di.AppContainer
import com.hits.notesapp.presentation.navigation.NotesNavGraph
import com.hits.notesapp.presentation.ui.NotesAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = AppContainer(applicationContext)
        ReminderReceiver.createNotificationChannel(applicationContext)
        requestNotificationPermissionIfNeeded()

        setContent {
            NotesAppTheme {
                NotesNavGraph(appContainer = appContainer)
            }
        }
    }
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            return
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
    }
}