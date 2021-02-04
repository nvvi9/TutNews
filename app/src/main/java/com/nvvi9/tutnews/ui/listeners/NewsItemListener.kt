package com.nvvi9.tutnews.ui.listeners

import com.nvvi9.tutnews.vo.NewsItem

interface NewsItemListener {
    fun onItemClicked(newsItem: NewsItem)
}