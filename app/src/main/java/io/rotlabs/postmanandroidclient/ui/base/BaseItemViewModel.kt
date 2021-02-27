package io.rotlabs.postmanandroidclient.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import java.time.Year

abstract class BaseItemViewModel<T : Any>(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    errorHelper: ErrorHelper,
    connectivityChecker: ConnectivityChecker
) : BaseViewModel(
    schedulerProvider, compositeDisposable, errorHelper, connectivityChecker
) {

    private val _data = MutableLiveData<T>()
    val data: LiveData<T> = _data

    fun updateData(newData: T) {
        _data.postValue(newData)
    }

    fun onManualCleared() {
        onCleared()
    }
}