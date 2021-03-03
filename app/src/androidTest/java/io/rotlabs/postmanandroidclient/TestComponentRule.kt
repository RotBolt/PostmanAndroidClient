package io.rotlabs.postmanandroidclient

import android.content.Context
import io.rotlabs.postmanandroidclient.di.component.DaggerApplicationComponent
import io.rotlabs.postmanandroidclient.di.component.DaggerTestAppComponent
import io.rotlabs.postmanandroidclient.di.component.TestAppComponent
import io.rotlabs.postmanandroidclient.di.module.TestAppModule
import io.rotlabs.postmanandroidclient.di.modules.AppModule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestComponentRule(private val context: Context) : TestRule {

    var testComponent: TestAppComponent? = null

    fun getContext() = context

    fun setupDaggerTestComponentInApplication() {
        val application = context.applicationContext as PostmanApp
        testComponent = DaggerTestAppComponent.builder()
            .applicationContext(getContext())
            .appModule(TestAppModule())
            .build()
        application.setAppComponent(testComponent!!)
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                try {
                    setupDaggerTestComponentInApplication()
                    base.evaluate()
                } finally {
                    testComponent = null
                }
            }
        }
    }
}