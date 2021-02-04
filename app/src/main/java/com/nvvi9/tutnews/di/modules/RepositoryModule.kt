package com.nvvi9.tutnews.di.modules

import com.nvvi9.tutnews.repositories.NewsDetailsRepositoryImpl
import com.nvvi9.tutnews.repositories.NewsRepositoryImpl
import com.nvvi9.tutnews.repositories.RecommendedNewsRepositoryImpl
import com.nvvi9.tutnews.repositories.base.NewsDetailsRepository
import com.nvvi9.tutnews.repositories.base.NewsRepository
import com.nvvi9.tutnews.repositories.base.RecommendedNewsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @Binds
    abstract fun bindNewsItemRepository(newsItemRepositoryImpl: NewsDetailsRepositoryImpl): NewsDetailsRepository

    @Binds
    abstract fun bindRecommendedNewsRepository(recommendedNewsRepositoryImpl: RecommendedNewsRepositoryImpl): RecommendedNewsRepository
}