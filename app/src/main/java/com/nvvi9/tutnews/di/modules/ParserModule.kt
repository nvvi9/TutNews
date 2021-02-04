package com.nvvi9.tutnews.di.modules

import com.nvvi9.tutnews.network.NewsParser
import dagger.Module
import dagger.Provides

@Module
class ParserModule {

    @Provides
    fun provideNewsParser() = NewsParser()
}