package io.rotlabs.postmanandroidclient.ui.makeRequest.response

import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.ui.base.BaseViewModel
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider

class ResponseViewModel(
    // TODO add local data store to save reponse details
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    val errorHelper: ErrorHelper,
    val connectivityChecker: ConnectivityChecker
) : BaseViewModel(schedulerProvider, compositeDisposable, errorHelper, connectivityChecker) {
    override fun onCreate() {

    }

}