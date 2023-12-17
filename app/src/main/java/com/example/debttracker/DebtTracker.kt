package com.example.debttracker

import android.app.Application

class DebtTracker : Application() {
    companion object {
        lateinit var instance: DebtTracker
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}