package com.nvvi9.tutnews.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nvvi9.tutnews.data.NewsInfo

@Database(entities = [NewsInfo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract val newsDao: NewsDao
}