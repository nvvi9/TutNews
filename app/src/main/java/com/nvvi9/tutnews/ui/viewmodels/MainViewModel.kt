package com.nvvi9.tutnews.ui.viewmodels

import androidx.lifecycle.*
import com.nvvi9.tutnews.data.LoadState
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.SortOrder
import com.nvvi9.tutnews.domain.NewsItemsUseCase
import com.nvvi9.tutnews.vo.NewsItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val newsItemsUseCase: NewsItemsUseCase,
) : ViewModel() {

    private val _loadState = MutableLiveData<LoadState>(LoadState.NotLoading())
    val loadState: LiveData<LoadState> = _loadState

    private val newsItemsByDateAscending = MediatorLiveData<List<NewsItem>>()

    private val newsItemsByDateDescending = MediatorLiveData<List<NewsItem>>()

    private val items = MediatorLiveData<List<NewsItem>>()

    private val itemsObserver = Observer<List<NewsItem>> {
        if (it.isNullOrEmpty()) {
            updateNews(false)
        }
    }

    private var sortOrder = SortOrder.DESCENDING

    private var currentNewsCategory = NewsCategory.MAIN_NEWS

    init {
        unmarkAllRecommended()
        newsItemsByDateAscending.addSource(
            newsItemsUseCase.getNewsItemsSortedByDateAscending(
                currentNewsCategory
            )
        ) { itemsList ->
            itemsList?.let {
                newsItemsByDateAscending.postValue(it)
            }
        }

        newsItemsByDateDescending.addSource(
            newsItemsUseCase.getNewsItemsSortedByDateDescending(
                currentNewsCategory
            )
        ) { itemsList ->
            itemsList?.let {
                newsItemsByDateDescending.postValue(it)
            }
        }

        items.addSource(newsItemsByDateAscending) { itemsList ->
            itemsList?.takeIf { sortOrder == SortOrder.ASCENDING }?.let { items.postValue(it) }
        }

        items.addSource(newsItemsByDateDescending) { itemsList ->
            itemsList?.takeIf { sortOrder == SortOrder.DESCENDING }?.let { items.postValue(it) }
        }

        items.observeForever(itemsObserver)
    }

    override fun onCleared() {
        items.removeObserver(itemsObserver)
        items.removeSource(newsItemsByDateAscending)
        items.removeSource(newsItemsByDateDescending)
        newsItemsByDateAscending.removeSource(
            newsItemsUseCase.getNewsItemsSortedByDateDescending(
                currentNewsCategory
            )
        )
        newsItemsByDateDescending.removeSource(
            newsItemsUseCase.getNewsItemsSortedByDateAscending(
                currentNewsCategory
            )
        )
    }

    fun changeNewsCategory(newsCategory: NewsCategory) {
        newsItemsByDateDescending.run {
            removeSource(newsItemsUseCase.getNewsItemsSortedByDateDescending(currentNewsCategory))
            addSource(newsItemsUseCase.getNewsItemsSortedByDateDescending(newsCategory)) { itemsList ->
                itemsList?.let {
                    newsItemsByDateDescending.postValue(it)
                }
            }
        }

        newsItemsByDateAscending.run {
            removeSource(newsItemsUseCase.getNewsItemsSortedByDateAscending(currentNewsCategory))
            addSource(newsItemsUseCase.getNewsItemsSortedByDateAscending(newsCategory)) { itemsList ->
                itemsList?.let {
                    newsItemsByDateAscending.postValue(it)
                }
            }
        }

        currentNewsCategory = newsCategory
    }

    fun changeNewsItemsOrder(order: SortOrder) {
        when (order) {
            SortOrder.ASCENDING -> newsItemsByDateAscending.value?.let {
                items.postValue(it)
            }
            SortOrder.DESCENDING -> newsItemsByDateDescending.value?.let {
                items.postValue(it)
            }
        }

        sortOrder = order
    }

    fun observeOnNewsItems(lifecycleOwner: LifecycleOwner, observer: Observer<List<NewsItem>>) {
        items.observe(lifecycleOwner, observer)
    }

    fun updateNews(isRecommendation: Boolean) {
        newsItemsUseCase.updateNewsInfo(isRecommendation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _loadState.postValue(LoadState.Loading())
            }.subscribe({
                _loadState.postValue(LoadState.NotLoading())
            }, {
                _loadState.postValue(LoadState.Error(it))
            })
    }

    private fun unmarkAllRecommended() {
        newsItemsUseCase.unmarkAllRecommended()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}
