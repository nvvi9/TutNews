package com.nvvi9.tutnews.ui.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nvvi9.tutnews.R
import com.nvvi9.tutnews.data.LoadState
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.SortOrder
import com.nvvi9.tutnews.databinding.ActivityMainBinding
import com.nvvi9.tutnews.service.NewsUpdateService
import com.nvvi9.tutnews.ui.adapters.NewsItemAdapter
import com.nvvi9.tutnews.ui.listeners.NewsItemListener
import com.nvvi9.tutnews.ui.viewmodels.MainViewModel
import com.nvvi9.tutnews.vo.NewsItem
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity :
    AppCompatActivity(),
    NewsItemListener,
    ServiceConnection,
    AdapterView.OnItemSelectedListener,
    NewsUpdateService.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    private var itemsChanged = false

    private val newsItemAdapter = NewsItemAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        setupBinding()
        setupViewModelObservers()
        bindService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_date_sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_sort_date_ascending -> {
                mainViewModel.changeNewsItemsOrder(SortOrder.ASCENDING)
                itemsChanged = true
                true
            }
            R.id.menu_sort_date_descending -> {
                mainViewModel.changeNewsItemsOrder(SortOrder.DESCENDING)
                itemsChanged = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onItemClicked(newsItem: NewsItem) {
        val intent = Intent(this, NewsDetailsActivity::class.java)
        intent.putExtra("id", newsItem.id)
        startActivity(intent)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        (service as NewsUpdateService.NewsUpdateServiceBinder).newsUpdateService.callback = this
    }

    override fun onServiceDisconnected(name: ComponentName?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        NewsCategory.getByCategoryName(NewsCategory.values()[position].categoryName)?.let {
            itemsChanged = true
            mainViewModel.changeNewsCategory(it)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun updatesInCategoriesAreAvailable(categories: List<NewsCategory>) {
        if (mainViewModel.loadState.value !is LoadState.Loading) {
            Snackbar.make(
                binding.coordinatorLayout,
                getString(
                    R.string.update_recommendation_message,
                    categories.joinToString { it.categoryName }
                ),
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.update_action)) {
                mainViewModel.updateNews(true)
                itemsChanged = true
            }.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
        }
    }

    private fun bindService() {
        Intent(this, NewsUpdateService::class.java).also {
            bindService(it, this, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        unbindService(this)
    }

    private fun setupBinding() {
        setSupportActionBar(binding.toolbar)
        binding.run {
            categorySpinner.adapter = ArrayAdapter(
                categorySpinner.context,
                R.layout.news_category_item,
                NewsCategory.values().map { it.categoryName })

            categorySpinner.onItemSelectedListener = this@MainActivity
            newsItemView.adapter = newsItemAdapter
            swipeRefresh.setOnRefreshListener {
                mainViewModel.updateNews(false)
                itemsChanged = true
            }
            lifecycleOwner = this@MainActivity
        }
    }

    private fun setupViewModelObservers() {
        mainViewModel.run {
            observeOnNewsItems(this@MainActivity) { items ->
                items?.let {
                    newsItemAdapter.submitList(it) {
                        if (itemsChanged) {
                            binding.newsItemView.layoutManager?.scrollToPosition(0)
                            itemsChanged = false
                        }
                    }
                }
            }

            loadState.observe(this@MainActivity) {
                when (it) {
                    is LoadState.NotLoading -> {
                        binding.newsItemView.layoutManager?.scrollToPosition(0)
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is LoadState.Loading -> binding.swipeRefresh.isRefreshing = true
                    is LoadState.Error -> {
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}