package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueConfig
import okhttp3.Response

/**
 *  Shared ViewModel for communication between fragment and activity.
 *  Also to observe changes in the RequestConfig across different Request Config Fragments
 *  Since its ViewModel its tied to lifecycle of parent activity or fragment
 */
class MakeRequestSharedViewModel : ViewModel() {

    /**
     *  holds the QueryParams Key Value configs and emit changes to observers
     */
    val paramList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())

    /**
     *  holds the Header Key Value configs and emit changes to observers
     */
    val headerList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())

    /**
     *  holds the AuthInfo and emit changes to observers
     */
    val authInfo = MutableLiveData<AuthInfo>()

    /**
     *  holds the BodyInfo and emit changes to observers
     */
    val bodyInfo = MutableLiveData<BodyInfo>()

    val response = MutableLiveData<Response>()

    val responseText = MutableLiveData<String>()

    /**
     *  To share request progress accross ReponseBottomSheet
     */
    val requestProgress = MutableLiveData<Boolean>()
}