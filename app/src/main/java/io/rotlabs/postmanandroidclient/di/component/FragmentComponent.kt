package io.rotlabs.postmanandroidclient.di.component

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Component
import io.rotlabs.postmanandroidclient.di.FragmentScope
import io.rotlabs.postmanandroidclient.di.modules.FragmentModule
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.AddKeyValueBottomSheet
import io.rotlabs.postmanandroidclient.ui.makeRequest.auth.AuthInfoFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.header.HeaderFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.params.ParamsFragment

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun fragment(fragment: Fragment): Builder
        fun fragmentModule(fragmentModule: FragmentModule): Builder
        fun appComponent(applicationComponent: ApplicationComponent): Builder
        fun build(): FragmentComponent
    }

    fun inject(fragment: AddKeyValueBottomSheet)

    fun inject(fragment: ParamsFragment)

    fun inject(fragment:HeaderFragment)

    fun inject(fragment: AuthInfoFragment)

}