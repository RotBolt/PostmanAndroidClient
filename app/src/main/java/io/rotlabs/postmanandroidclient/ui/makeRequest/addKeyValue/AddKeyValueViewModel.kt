package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.ui.base.BaseItemViewModel
import io.rotlabs.postmanandroidclient.ui.base.BaseViewModel
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import javax.inject.Inject

class AddKeyValueViewModel(
    // TODO ADD LOCAL REPOSITORY
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    errorHelper: ErrorHelper,
    connectivityChecker: ConnectivityChecker
) : BaseItemViewModel<KeyValueConfig>(
    schedulerProvider, compositeDisposable, errorHelper, connectivityChecker
) {
    override fun onCreate() {}
}