package com.nvvi9.tutnews.di.modules

import com.nvvi9.tutnews.di.scopes.ActivityScope
import com.nvvi9.tutnews.domain.NewsItemsUseCase
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UseCaseModule {

    @ContributesAndroidInjector
    @ActivityScope
    abstract fun contributeNewsItemsUseCase(): NewsItemsUseCase
}