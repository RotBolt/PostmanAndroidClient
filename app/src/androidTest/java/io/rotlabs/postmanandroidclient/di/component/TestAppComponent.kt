package io.rotlabs.postmanandroidclient.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.rotlabs.postmanandroidclient.di.ApplicationContext
import io.rotlabs.postmanandroidclient.di.module.TestAppModule
import io.rotlabs.postmanandroidclient.di.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [TestAppModule::class]
)
interface TestAppComponent : ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@ApplicationContext appContext: Context): Builder

        fun appModule(testAppModule: TestAppModule): Builder

        fun build(): TestAppComponent
    }
}