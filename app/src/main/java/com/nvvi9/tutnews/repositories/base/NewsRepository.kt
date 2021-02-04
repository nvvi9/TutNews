package com.nvvi9.tutnews.repositories.base

import androidx.lifecycle.LiveData
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.NewsInfo
import io.reactivex.rxjava3.core.Completable

interface NewsRepository {
    /**
     * Updates all news in database
     */
    fun updateNewsInfo(isRecommendation: Boolean): Completable

    /**
     * Returns list of NewsInfo
     * @param newsCategory category of NewsInfo items
     * @return LiveData of NewsInfo items
     */
    fun getNewsInfo(newsCategory: NewsCategory): LiveData<List<NewsInfo>>

    /**
     * Removes all marks which flag news added by recommendation
     */
    fun unmarkAllRecommended(): Completable
}