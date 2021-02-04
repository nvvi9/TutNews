package com.nvvi9.tutnews.di.modules

import com.nvvi9.tutnews.service.NewsUpdateService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun bindNewsUpdateService(): NewsUpdateService
}