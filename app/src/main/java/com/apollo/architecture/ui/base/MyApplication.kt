package com.apollo.architecture.ui.base

import android.app.Application
import com.apollo.architecture.di.appModule
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}