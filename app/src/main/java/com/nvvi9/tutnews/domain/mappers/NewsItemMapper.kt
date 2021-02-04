package com.nvvi9.tutnews.domain.mappers

import com.nvvi9.tutnews.data.BaseMapper
import com.nvvi9.tutnews.data.NewsInfo
import com.nvvi9.tutnews.vo.NewsItem

object NewsItemMapper : BaseMapper<NewsInfo, NewsItem> {

    override fun map(value: NewsInfo): NewsItem =
        value.run {
            NewsItem(
                id, title, authors, publicationTime,
                newsCategory, isViewed, isUpdatedByRecommendation
            )
        }
}