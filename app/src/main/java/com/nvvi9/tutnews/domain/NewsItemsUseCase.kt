package com.nvvi9.tutnews.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.domain.mappers.NewsItemMapper
import com.nvvi9.tutnews.repositories.base.NewsRepository
import com.nvvi9.tutnews.vo.NewsItem
import javax.inject.Inject

class NewsItemsUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    /**
     * Updates all news in database
     */
    fun updateNewsInfo(isRecommendation: Boolean) =
        newsRepository.updateNewsInfo(isRecommendation)

    /**
     * Maps LiveData of NewsInfo list to NewsItem list
     * @param newsCategory category of news items
     * @return LiveData of news sorted by date in ascending order
     */
    fun getNewsItemsSortedByDateAscending(newsCategory: NewsCategory): LiveData<List<NewsItem>> =
        Transformations.map(getNewsItemsByCategory(newsCategory)) { items ->
            items.sortedBy { it.publicationTime }
        }

    /**
     * Maps LiveData of NewsInfo list to NewsItem list
     * @param newsCategory category of news items
     * @return LiveData of news sorted by date in descending order
     */
    fun getNewsItemsSortedByDateDescending(newsCategory: NewsCategory): LiveData<List<NewsItem>> =
        Transformations.map(getNewsItemsByCategory(newsCategory)) { items ->
            items.sortedByDescending { it.publicationTime }
        }

    /**
     * Removes all marks which flag news added by recommendation
     */
    fun unmarkAllRecommended() = newsRepository.unmarkAllRecommended()

    private fun getNewsItemsByCategory(newsCategory: NewsCategory): LiveData<List<NewsItem>> =
        Transformations.map(newsRepository.getNewsInfo(newsCategory)) { items ->
            items.map { NewsItemMapper.map(it) }
        }
}