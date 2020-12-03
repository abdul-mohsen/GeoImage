package com.bignerdranch.android.geoimage

import android.app.Application
import com.squareup.picasso.Picasso
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Picasso.get().setIndicatorsEnabled(true)
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}