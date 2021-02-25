package io.rotlabs.postmanandroidclient.di.component

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.di.ApplicationContext
import io.rotlabs.postmanandroidclient.di.modules.AppModule
import okhttp3.OkHttpClient

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

    fun inject(app: PostmanApp)
}