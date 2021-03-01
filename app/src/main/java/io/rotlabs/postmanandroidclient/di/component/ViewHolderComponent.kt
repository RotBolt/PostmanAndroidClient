package io.rotlabs.postmanandroidclient.di.component

import dagger.BindsInstance
import dagger.Component
import io.rotlabs.postmanandroidclient.di.ViewHolderScope
import io.rotlabs.postmanandroidclient.di.modules.ViewHolderModule
import io.rotlabs.postmanandroidclient.ui.base.BaseItemViewHolder
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.AddKeyValueViewHolder

@ViewHolderScope
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

    fun inject(viewHolder: AddKeyValueViewHolder)
}