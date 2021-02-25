package io.rotlabs.postmanandroidclient.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import javax.inject.Inject

/**
 *  BaseViewModel class
 *  Holds the common functions and fields could be required the child class
 */

abstract class BaseViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    private val errorHelper: ErrorHelper,
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {


    /**
     *  holds the error message and  use to propagate the error message to view
     */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun isConnectedToInternet() = connectivityChecker.isConnected()

    /**
     *  Cast the exception to proper error message and propagate to the view
     */
    fun handleError(throwable: Throwable?) {
        val message = errorHelper.getErrorMessage(throwable)
        _errorMessage.postValue(message)
    }

    abstract fun onCreate()

    override fun onCleared() {
        // remove all the operations and retain resources when this viewmodel is cleared
        compositeDisposable.clear()
        super.onCleared()
    }
}