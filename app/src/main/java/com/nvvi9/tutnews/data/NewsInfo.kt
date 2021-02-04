package com.nvvi9.tutnews.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["link"], unique = true)])
data class NewsInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val link: String,
    var description: String,
    val thumbnailUri: String,
    val authors: List<String>,
    val publicationTime: Date,
    val newsCategory: NewsCategory,
    var isTopWeek: Boolean = false,
    var isViewed: Boolean = false,
    var isUpdatedByRecommendation: Boolean = false
)