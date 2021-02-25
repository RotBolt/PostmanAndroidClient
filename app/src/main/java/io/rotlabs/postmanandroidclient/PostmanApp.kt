package io.rotlabs.postmanandroidclient

import android.app.Application
import io.rotlabs.postmanandroidclient.di.component.ApplicationComponent
import io.rotlabs.postmanandroidclient.di.component.DaggerApplicationComponent
import io.rotlabs.postmanandroidclient.di.modules.AppModule

class PostmanApp : Application() {

    /**
     *  Application component to provide dependencies to other components and other classes
     */
    private lateinit var appComponent: ApplicationComponent

    fun getAppComponent() = appComponent

    /**
     *   function to set the custom appComponent
     *   Eg. to set the TestAppComponent in application
     */
    fun setAppComponent(appComponent: ApplicationComponent) {
        this.appComponent = appComponent
    }


    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        appComponent = DaggerApplicationComponent.builder()
            .applicationContext(this)
            .appModule(AppModule())
            .build()

        appComponent.inject(this)
    }
}