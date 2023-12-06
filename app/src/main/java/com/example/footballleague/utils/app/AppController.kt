package com.example.footballleague.utils.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class AppController : Application() {
    companion object {
        lateinit var instance: AppController
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
}