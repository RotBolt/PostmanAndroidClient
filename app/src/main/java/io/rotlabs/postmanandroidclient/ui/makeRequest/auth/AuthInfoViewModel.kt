package io.rotlabs.postmanandroidclient.ui.makeRequest.auth

import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.ui.base.BaseViewModel
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider

class AuthInfoViewModel(
    // TODO add Local data store
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    errorHelper: ErrorHelper,
    connectivityChecker: ConnectivityChecker
) : BaseViewModel(schedulerProvider, compositeDisposable, errorHelper, connectivityChecker) {
    override fun onCreate() {

    }
}