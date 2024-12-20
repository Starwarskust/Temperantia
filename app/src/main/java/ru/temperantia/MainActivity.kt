package ru.temperantia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemperantiaApp()
        }
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(
            24, TimeUnit.HOURS,
            1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this)
            .enqueue(workRequest)
    }
}