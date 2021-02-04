package com.nvvi9.tutnews.di.modules

import android.content.Context
import androidx.room.Room
import com.nvvi9.tutnews.db.NewsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context) =
        Room.databaseBuilder(context, NewsDatabase::class.java, "tutnews.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideNewsDao(newsDatabase: NewsDatabase) = newsDatabase.newsDao
}