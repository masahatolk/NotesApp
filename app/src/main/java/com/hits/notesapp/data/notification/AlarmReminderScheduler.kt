package com.hits.notesapp.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hits.notesapp.domain.model.Note
import com.hits.notesapp.domain.notification.ReminderScheduler

class AlarmReminderScheduler(
    private val context: Context
) : ReminderScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(note: Note) {
        val reminderAt = note.reminderAt ?: return
        if (reminderAt <= System.currentTimeMillis()) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_NOTE_ID, note.id)
            putExtra(ReminderReceiver.EXTRA_TITLE, note.title?.ifBlank { "Напоминание о заметке" })
            putExtra(ReminderReceiver.EXTRA_CONTENT, note.content?.ifBlank { "Откройте заметку" })
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            note.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderAt,
            pendingIntent
        )
    }
}