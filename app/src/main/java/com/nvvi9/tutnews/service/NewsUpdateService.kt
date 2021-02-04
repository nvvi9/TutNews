package com.nvvi9.tutnews.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.repositories.base.RecommendedNewsRepository
import dagger.android.AndroidInjection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Service this periodically checks new NewsInfo
 */

class NewsUpdateService : Service() {

    private val newsUpdateServiceBinder = NewsUpdateServiceBinder()
    private val handler = Handler(Looper.getMainLooper())

    private val logTag = this::class.simpleName

    var callback: Callback? = null

    @Inject
    lateinit var recommendedNewsRepository: RecommendedNewsRepository

    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder =
        newsUpdateServiceBinder

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        runnable = Runnable {
            handler.postDelayed(runnable, 300000)
            checkUpdates()
        }

        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun checkUpdates() {
        recommendedNewsRepository.getNewNewsInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newsItems ->
                if (!newsItems.isNullOrEmpty()) {
                    callback?.updatesInCategoriesAreAvailable(newsItems.map { it.newsCategory }
                        .toSet().toList())
                }
            }, {
                Log.e(logTag, it.stackTraceToString())
            })
    }

    inner class NewsUpdateServiceBinder : Binder() {

        val newsUpdateService: NewsUpdateService = this@NewsUpdateService
    }

    interface Callback {
        fun updatesInCategoriesAreAvailable(categories: List<NewsCategory>)
    }
}