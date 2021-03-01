package io.rotlabs.postmanandroidclient.di.component

import android.app.Application
import android.content.Context
import android.view.textservice.SpellCheckerInfo
import dagger.BindsInstance
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.di.ApplicationContext
import io.rotlabs.postmanandroidclient.di.modules.AppModule
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@ApplicationContext appContext: Context): Builder

        fun appModule(appModule: AppModule): Builder

        fun build(): ApplicationComponent
    }


    fun getOkHttpClient(): OkHttpClient

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable

    fun getErrorHelper(): ErrorHelper

    fun getConnectivityChecker(): ConnectivityChecker

    fun inject(app: PostmanApp)
}