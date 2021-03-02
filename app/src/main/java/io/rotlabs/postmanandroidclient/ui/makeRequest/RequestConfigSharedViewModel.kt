package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueConfig

class RequestConfigSharedViewModel : ViewModel() {

    val paramList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())

    val headerList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())
}