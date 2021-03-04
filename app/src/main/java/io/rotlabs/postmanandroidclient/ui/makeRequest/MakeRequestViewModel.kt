package io.rotlabs.postmanandroidclient.ui.makeRequest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.data.remote.RestClient
import io.rotlabs.postmanandroidclient.ui.base.BaseViewModel
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelper
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityChecker
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import okhttp3.Response
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

class MakeRequestViewModel(
    private val restClient: RestClient,
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    errorHelper: ErrorHelper,
    connectivityChecker: ConnectivityChecker
) : BaseViewModel(schedulerProvider, compositeDisposable, errorHelper, connectivityChecker) {

    private val _malformedUrl = MutableLiveData<String>()
    val malformedUrl: LiveData<String> = _malformedUrl

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _response = MutableLiveData<Response>()
    val response: LiveData<Response> = _response

    private val _openBottomSheet = MutableLiveData<Boolean>()
    val openBottomSheet: LiveData<Boolean> = _openBottomSheet

    override fun onCreate() {}

    fun makeRequest(
        url: String?,
        requestMethod: RequestMethod,
        queryParams: Map<String, String>,
        authInfo: AuthInfo,
        headers: Map<String, String>,
        bodyInfo: BodyInfo
    ) {
        if (!url.isNullOrEmpty() && isValidURL(url)) {
            _openBottomSheet.postValue(true)
            _progress.postValue(true)
            compositeDisposable.add(
                restClient.makeRequestCall(
                    url,
                    requestMethod,
                    queryParams,
                    authInfo,
                    headers,
                    bodyInfo
                )
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        _response.postValue(it)
                        _progress.postValue(false)
                    }, {
                        handleError(it)
                        _progress.postValue(false)
                    })
            )
        } else {
            _malformedUrl.postValue(RestClient.MALFORMED_URL)
        }
    }

    fun isValidURL(url: String?): Boolean {
        try {
            URL(url).toURI()
        } catch (e: MalformedURLException) {
            return false
        } catch (e: URISyntaxException) {
            return false
        }
        return true
    }


}