package io.rotlabs.postmanandroidclient.di.modules

import androidx.lifecycle.LifecycleRegistry
import dagger.Module
import dagger.Provides
import io.rotlabs.postmanandroidclient.ui.base.BaseItemViewHolder

@Module
class ViewHolderModule {

    @Provides
    fun provideLifecycleRegistry(viewHolder: BaseItemViewHolder<*, *>): LifecycleRegistry =
        LifecycleRegistry(viewHolder)
}