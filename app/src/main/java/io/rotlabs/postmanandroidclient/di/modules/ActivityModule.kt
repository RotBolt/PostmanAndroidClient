package io.rotlabs.postmanandroidclient.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.data.remote.RestClient
import io.rotlabs.postmanandroidclient.ui.base.BaseActivity
import io.rotlabs.postmanandroidclient.ui.makeRequest.MakeRequestViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.RequestConfigSharedViewModel
import io.rotlabs.postmanandroidclient.utils.ViewModelProviderFactory
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider

@Module
class ActivityModule {

    @Provides
    fun provideMakeRequestViewModel(
        activity: BaseActivity<*, *>,
        restClient: RestClient,
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        errorHelper: ErrorHelper,
        connectivityChecker: ConnectivityChecker
    ): MakeRequestViewModel {
        return ViewModelProvider(activity, ViewModelProviderFactory(MakeRequestViewModel::class) {
            MakeRequestViewModel(
                restClient,
                schedulerProvider,
                compositeDisposable,
                errorHelper,
                connectivityChecker
            )
        }).get(MakeRequestViewModel::class.java)
    }

    @Provides
    fun provideRequestConfigSharedViewModel(activity: BaseActivity<*, *>): RequestConfigSharedViewModel {
        return ViewModelProvider(
            activity,
            ViewModelProviderFactory(RequestConfigSharedViewModel::class) {
                RequestConfigSharedViewModel()
            }).get(RequestConfigSharedViewModel::class.java)
    }

}