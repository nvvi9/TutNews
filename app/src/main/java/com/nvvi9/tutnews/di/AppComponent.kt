package com.nvvi9.tutnews.di

import android.app.Application
import android.content.Context
import com.nvvi9.tutnews.TutNewsApplication
import com.nvvi9.tutnews.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
        ParserModule::class,
        ServiceModule::class
    ]
)
interface AppComponent : AndroidInjector<TutNewsApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: TutNewsApplication?)
}