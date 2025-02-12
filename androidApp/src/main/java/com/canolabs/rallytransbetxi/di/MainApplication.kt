package com.canolabs.rallytransbetxi.di

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MainApplication", "Proceeding to: Firebase initialization")
        FirebaseApp.initializeApp(this)
        Log.d("MainApplication", "Proceeding to: Firebase initialized")

        Firebase.appCheck.getAppCheckToken(false).addOnSuccessListener {
            Log.d("MainApplication", "App Check token: ${it.token}")
        }.addOnFailureListener {
            Log.d("MainApplication", "App Check token error: ${it.message}")
        }
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        Log.d("MainApplication", "Proceeding to: App Check provider factory installed")

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
            modules(databaseModule)
            modules(networkModule)
            modules(firebaseModule)
        }
    }
}