package com.nvvi9.tutnews.repositories;

import com.nvvi9.tutnews.data.NewsInfo;
import com.nvvi9.tutnews.db.NewsDao;
import com.nvvi9.tutnews.repositories.base.NewsDetailsRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class NewsDetailsRepositoryImpl implements NewsDetailsRepository {

    private final NewsDao newsDao;

    @Inject
    public NewsDetailsRepositoryImpl(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Override
    public Single<NewsInfo> getNewsInfoById(int id) {
        return newsDao.getNewsInfoById(id);
    }

    @Override
    public Single<NewsInfo> updateNewsInfo(NewsInfo newsInfo) {
        return newsDao.update(newsInfo)
                .andThen(newsDao.getNewsInfoById(newsInfo.getId()));
    }
}
