package com.nvvi9.tutnews.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nvvi9.tutnews.data.LoadState;
import com.nvvi9.tutnews.data.NewsInfo;
import com.nvvi9.tutnews.repositories.base.NewsDetailsRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsDetailsViewModel extends ViewModel {

    private final NewsDetailsRepository newsDetailsRepository;

    private final MutableLiveData<LoadState> loadStateMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<NewsInfo> newsItemMutableLiveData = new MutableLiveData<>();

    @Inject
    public NewsDetailsViewModel(NewsDetailsRepository newsDetailsRepository) {
        this.newsDetailsRepository = newsDetailsRepository;
    }

    public LiveData<LoadState> getLoadState() {
        return loadStateMutableLiveData;
    }

    public LiveData<NewsInfo> getNewsItem() {
        return newsItemMutableLiveData;
    }

    public void setNewsItem(int id) {
        newsDetailsRepository.getNewsInfoById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> loadStateMutableLiveData.postValue(new LoadState.Loading()))
                .subscribe(newsItem -> {
                    loadStateMutableLiveData.postValue(new LoadState.NotLoading());
                    newsItemMutableLiveData.postValue(newsItem);
                    markAsViewed(newsItem);
                }, throwable -> {
                    loadStateMutableLiveData.postValue(new LoadState.Error(throwable.getMessage()));
                });
    }

    public void updateNewsItemDescription(String description) {
        NewsInfo newsInfo = newsItemMutableLiveData.getValue();
        if (newsInfo != null) {
            newsInfo.setDescription(description);
            newsItemMutableLiveData.postValue(newsInfo);
            updateNewsInfo(newsInfo);
        }
    }

    private void markAsViewed(NewsInfo newsInfo) {
        newsInfo.setUpdatedByRecommendation(false);
        newsInfo.setViewed(true);
        updateNewsInfo(newsInfo);
    }

    private void updateNewsInfo(NewsInfo newsInfo) {
        newsDetailsRepository.updateNewsInfo(newsInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> loadStateMutableLiveData.postValue(new LoadState.Loading()))
                .subscribe(item -> {
                    loadStateMutableLiveData.postValue(new LoadState.NotLoading());
                    newsItemMutableLiveData.postValue(item);
                }, throwable -> {
                    loadStateMutableLiveData.postValue(new LoadState.Error(throwable.getMessage()));
                });
    }
}
