package com.findyou_professionalapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class AppActivity : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val firebaseAppCheck= FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory (
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}