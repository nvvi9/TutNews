package com.nvvi9.tutnews.network

import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.NewsInfo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TutByDataSource @Inject constructor(private val newsParser: NewsParser) {

    /**
     * Returns all Single that contains list of NewsInfo for all categories or empty list if error occurs
     * @return Single containing list of NewsInfo
     */
    fun getNewsInfo(): Single<List<NewsInfo>> =
        Observable.fromIterable(NewsCategory.values().filter { it != NewsCategory.MAIN_NEWS })
            .subscribeOn(Schedulers.io())
            .flatMapSingle { newsParser.parseNews(it.urlRoute) }
            .toList()
            .map { it.flatten() }
            .zipWith(newsParser.parseNews(NewsCategory.MAIN_NEWS.urlRoute)) { allNews, mainNews ->
                allNews.onEach { newsItem ->
                    newsItem.takeIf { item -> mainNews.any { item == it } }?.let {
                        it.isTopWeek = true
                    }
                }
            }.onErrorReturnItem(emptyList())
}