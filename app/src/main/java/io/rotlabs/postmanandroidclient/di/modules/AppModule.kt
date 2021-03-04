package io.rotlabs.postmanandroidclient.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.di.ApplicationContext
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueConfigDataHolder
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelperImpl
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityCheckerImpl
import io.rotlabs.postmanandroidclient.utils.rx.RxSchedulerProvider
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
//            .cache(cache)
            .build()
    }


    @Provides
    @Singleton
    fun provideCache(@ApplicationContext appContext: Context): Cache =
        Cache(appContext.cacheDir, 10 * 1024 * 1024)

    @Provides
    @Singleton
    fun providesScheduler(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    fun providesCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun providesErrorHelper(): ErrorHelper = ErrorHelperImpl()

    @Provides
    @Singleton
    fun providesConnectivityChecker(@ApplicationContext appContext: Context): ConnectivityChecker =
        ConnectivityCheckerImpl(appContext)

    @Provides
    @Singleton
    fun providesKeyValueConfigDataHolder(): KeyValueConfigDataHolder = KeyValueConfigDataHolder()

}