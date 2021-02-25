package io.rotlabs.postmanandroidclient.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import io.rotlabs.postmanandroidclient.di.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideOkHttpClient(
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .cache(cache)
            .build()
    }


    @Provides
    fun provideCache(@ApplicationContext appContext: Context): Cache =
        Cache(appContext.cacheDir, 10 * 1024 * 1024)

}