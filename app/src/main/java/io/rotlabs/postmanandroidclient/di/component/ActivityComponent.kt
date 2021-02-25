package io.rotlabs.postmanandroidclient.di.component

import dagger.BindsInstance
import dagger.Component
import io.rotlabs.postmanandroidclient.di.modules.ActivityModule
import io.rotlabs.postmanandroidclient.ui.base.BaseActivity

@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: BaseActivity<*, *>): Builder
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun activityModule(activityModule: ActivityModule): Builder
        fun build(): ActivityComponent
    }

}