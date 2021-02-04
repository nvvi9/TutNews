package com.nvvi9.tutnews.di.modules

import com.nvvi9.tutnews.di.scopes.ActivityScope
import com.nvvi9.tutnews.ui.activities.MainActivity
import com.nvvi9.tutnews.ui.activities.NewsDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeNewsDetailsActivity(): NewsDetailsActivity
}