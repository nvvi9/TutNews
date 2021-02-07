package com.nvvi9.tutnews.repositories

import androidx.lifecycle.LiveData
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.NewsInfo
import com.nvvi9.tutnews.db.NewsDao
import com.nvvi9.tutnews.network.TutByDataSource
import com.nvvi9.tutnews.repositories.base.NewsRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val tutByDataSource: TutByDataSource,
    private val newsDao: NewsDao,
) : NewsRepository {

    override fun updateNewsInfo(isRecommendation: Boolean): Completable =
        tutByDataSource.getNewsInfo()
            .map { newsInfo -> newsInfo.onEach { it.isUpdatedByRecommendation = isRecommendation } }
            .concatMapCompletable { if (it.isNotEmpty()) insertNewsInfo(it) else Completable.complete() }

    override fun getNewsInfo(newsCategory: NewsCategory): LiveData<List<NewsInfo>> =
        when (newsCategory) {
            NewsCategory.MAIN_NEWS -> newsDao.getMainNewsInfo()
            else -> newsDao.getNewsInfoByCategory(newsCategory)
        }

    override fun unmarkAllRecommended(): Completable =
        newsDao.getNewsInfo()
            .concatMapCompletable { newsInfo ->
                newsDao.update(newsInfo.onEach { it.isUpdatedByRecommendation = false })
            }

    private fun insertNewsInfo(newsInfo: List<NewsInfo>) =
        newsDao.getNewsInfo()
            .map { oldNews ->
                oldNews.filter { oldItem ->
                    newsInfo.none { it.link == oldItem.link }
                } to oldNews.mapNotNull { oldItem ->
                    newsInfo.find { oldItem.link == it.link }?.let {
                        oldItem.copy(
                            title = it.title,
                            thumbnailUri = it.thumbnailUri,
                            authors = it.authors,
                            publicationTime = it.publicationTime,
                            isTopWeek = it.isTopWeek,
                        )
                    }
                }
            }.concatMapCompletable {
                newsDao.delete(it.first)
                    .andThen(newsDao.update(it.second))
                    .andThen(newsDao.insert(newsInfo - it.second))
            }
}