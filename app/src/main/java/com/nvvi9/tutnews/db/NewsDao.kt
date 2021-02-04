package com.nvvi9.tutnews.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.NewsInfo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(newsInfo: List<NewsInfo>): Completable

    @Update
    fun update(newsInfo: List<NewsInfo>): Completable

    @Update
    fun update(newsInfo: NewsInfo): Completable

    @Query("SELECT * FROM NewsInfo WHERE id=:id")
    fun getNewsInfoById(id: Int): Single<NewsInfo>

    @Query("SELECT * FROM NewsInfo WHERE newsCategory=:newsCategory")
    fun getNewsInfoByCategory(newsCategory: NewsCategory): LiveData<List<NewsInfo>>

    @Query("SELECT * FROM NewsInfo WHERE isTopWeek=1")
    fun getMainNewsInfo(): LiveData<List<NewsInfo>>

    @Query("SELECT * FROM NewsInfo")
    fun getNewsInfo(): Single<List<NewsInfo>>

    @Delete
    fun delete(newsInfo: List<NewsInfo>): Completable

    @Query("DELETE FROM NewsInfo")
    fun clear(): Completable
}