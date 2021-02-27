package io.rotlabs.postmanandroidclient.di.component

import dagger.BindsInstance
import dagger.Component
import io.rotlabs.postmanandroidclient.di.modules.ViewHolderModule
import io.rotlabs.postmanandroidclient.ui.base.BaseItemViewHolder

@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun viewHolder(viewHolder: BaseItemViewHolder<*, *>): Builder
        fun viewHolderModule(viewHolderModule: ViewHolderModule): Builder
        fun appComponent(appComponent: ApplicationComponent): Builder
        fun build(): ViewHolderComponent
    }
}