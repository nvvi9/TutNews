package com.nvvi9.tutnews

import android.app.Application
import com.nvvi9.tutnews.di.DaggerAppComponent
import com.nvvi9.tutnews.network.NetworkMonitor
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class TutNewsApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        NetworkMonitor(this).startNetworkCallback()
        DaggerAppComponent.builder()
            .application(this)
            .context(this)
            .build()
            .inject(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}