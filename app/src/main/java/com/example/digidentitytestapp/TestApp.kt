package com.example.digidentitytestapp

import android.app.Application
import com.example.digidentitytestapp.di.mainModule
import com.example.digidentitytestapp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@TestApp)
            modules(
                mainModule(this@TestApp),
                networkModule()
            )
        }
    }
}