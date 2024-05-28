package com.artistle.navkitexample

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MapKitExampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}