package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueConfig

/**
 *  Shared ViewModel for communication between fragment and activity.
 *  Also to observe changes in the RequestConfig across different Request Config Fragments
 *  Since its ViewModel its tied to lifecyle of parent activity or fragment
 */
class RequestConfigSharedViewModel : ViewModel() {

    /**
     *  holds the QueryParams Key Value configs
     */
    val paramList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())

    /**
     *  holds the Header Key Value configs
     */
    val headerList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())
}