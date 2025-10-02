package com.ddn.waypilot

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializa Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.maps_api_key))
        }
    }
}
