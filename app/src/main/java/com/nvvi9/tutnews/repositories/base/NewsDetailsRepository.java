package com.nvvi9.tutnews.repositories.base;

import com.nvvi9.tutnews.data.NewsInfo;

import io.reactivex.rxjava3.core.Single;

public interface NewsDetailsRepository {

    /**
     * Returns Single NewsInfo from database by its id
     *
     * @param id id of NewsInfo
     * @return {@link Single} that emits NewsItem
     */
    Single<NewsInfo> getNewsInfoById(int id);

    /**
     * Updates given {@code newsInfo} instance in database
     *
     * @param newsInfo item to update
     * @return {@link Single} that emits updated {@code newsInfo}
     */
    Single<NewsInfo> updateNewsInfo(NewsInfo newsInfo);
}
