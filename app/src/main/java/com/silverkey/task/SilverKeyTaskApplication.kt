package com.silverkey.task

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SilverKeyTaskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: SilverKeyTaskApplication
            private set
    }

}