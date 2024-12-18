package ru.temperantia

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
    companion object {
        lateinit var appContext: Context
    }
}