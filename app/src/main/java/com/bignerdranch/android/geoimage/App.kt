package com.bignerdranch.android.geoimage

import android.app.Application
import com.squareup.picasso.Picasso

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Picasso.get().setIndicatorsEnabled(true)
    }
}