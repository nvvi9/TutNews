package com.nvvi9.tutnews.repositories

import com.nvvi9.tutnews.data.NewsInfo
import com.nvvi9.tutnews.db.NewsDao
import com.nvvi9.tutnews.network.TutByDataSource
import com.nvvi9.tutnews.repositories.base.RecommendedNewsRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RecommendedNewsRepositoryImpl @Inject constructor(
    private val tutByDataSource: TutByDataSource,
    private val newsDao: NewsDao
) : RecommendedNewsRepository {

    override fun getNewNewsInfo(): Single<List<NewsInfo>> =
        tutByDataSource.getNewsInfo()
            .zipWith(newsDao.getNewsInfo()) { newNews, oldNews ->
                newNews.filter { newItem ->
                    oldNews.none { it.link == newItem.link }
                }.onEach { it.isUpdatedByRecommendation = true }
            }
}