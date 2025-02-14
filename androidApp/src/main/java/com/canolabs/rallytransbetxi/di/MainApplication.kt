package com.canolabs.rallytransbetxi.di

import android.app.Application
import android.util.Log
import com.canolabs.rallytransbetxi.shared.di.initKoin
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
//import dev.gitlive.firebase.crashlytics.crashlytics
//import com.google.firebase.FirebaseApp
//import com.google.firebase.appcheck.appCheck
//import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase.initialize(this)
        Log.d("MainApplication", "Proceeding to initkoin")
        initKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@MainApplication)
        }

        // TODO Set up crashlytics in the KMP project by uncommenting the gradle plugin and the following line
        // Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        /*
        Firebase.appCheck.getAppCheckToken(false).addOnSuccessListener {
            Log.d("MainApplication", "App Check token: ${it.token}")
        }.addOnFailureListener {
            Log.d("MainApplication", "App Check token error: ${it.message}")
        }
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        Log.d("MainApplication", "Proceeding to: App Check provider factory installed")
        */

        /*startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
            modules(databaseModule)
            modules(networkModule)
            modules(firebaseModule)
        }*/
    }
}