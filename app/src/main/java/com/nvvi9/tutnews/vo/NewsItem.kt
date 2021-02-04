package com.nvvi9.tutnews.vo

import com.nvvi9.tutnews.data.NewsCategory
import java.util.*

data class NewsItem(
    val id: Int,
    val title: String,
    val authors: List<String>,
    val publicationTime: Date,
    val newsCategory: NewsCategory,
    val isViewed: Boolean,
    val isUpdatedByRecommendation: Boolean
)