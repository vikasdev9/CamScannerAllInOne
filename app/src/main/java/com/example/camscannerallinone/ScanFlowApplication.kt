package com.example.camscannerallinone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.opencv.android.OpenCVLoader
import timber.log.Timber

@HiltAndroidApp
class ScanFlowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize OpenCV
        if (OpenCVLoader.initLocal()) {
            Timber.i("OpenCV loaded successfully")
        } else {
            Timber.e("OpenCV initialization failed!")
        }
    }
}
