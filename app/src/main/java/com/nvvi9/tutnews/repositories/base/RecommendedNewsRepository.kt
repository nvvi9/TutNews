package com.nvvi9.tutnews.repositories.base

import com.nvvi9.tutnews.data.NewsInfo
import io.reactivex.rxjava3.core.Single

interface RecommendedNewsRepository {
    /**
     * Returns Single that emits list of NewsInfo, that are not yet in database
     */
    fun getNewNewsInfo(): Single<List<NewsInfo>>
}