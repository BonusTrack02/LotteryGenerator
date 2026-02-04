package com.bonustrack02.lotterygenerator.di

import android.app.Application
import com.bonustrack02.lotterygenerator.BuildConfig
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled =
            false

        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@App) {}
        }
    }
}