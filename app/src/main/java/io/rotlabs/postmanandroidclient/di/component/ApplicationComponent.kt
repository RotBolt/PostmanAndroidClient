package io.rotlabs.postmanandroidclient.di.component

import android.app.Application
import dagger.Component
import io.rotlabs.postmanandroidclient.di.modules.AppModule

@Component(modules = [AppModule::class])
interface ApplicationComponent {

    interface Builder {
        fun application(app: Application): Builder

        fun appModule(appModule: AppModule): Builder

        fun build(): ApplicationComponent
    }

}