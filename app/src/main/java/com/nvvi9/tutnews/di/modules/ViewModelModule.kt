package com.nvvi9.tutnews.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nvvi9.tutnews.di.factories.ViewModelFactory
import com.nvvi9.tutnews.di.keys.ViewModelKey
import com.nvvi9.tutnews.ui.viewmodels.MainViewModel
import com.nvvi9.tutnews.ui.viewmodels.NewsDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailsViewModel::class)
    abstract fun bindNewsDetailsViewModel(newsDetailsViewModel: NewsDetailsViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}