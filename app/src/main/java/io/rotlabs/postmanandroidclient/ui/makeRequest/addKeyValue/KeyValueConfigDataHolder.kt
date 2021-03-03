package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import androidx.lifecycle.MutableLiveData

/**
 *  Data holder class to hold Key Value configs in AddKeyValueLayout
 *  @see AddKeyValueLayout
 *  Also provide gives the ability to observe the changes in Key Value configs
 */
class KeyValueConfigDataHolder {

    /**
     *  holds the QueryParams Key Value configs
     */
    val paramList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())

    /**
     *  holds the Header Key Value configs
     */
    val headerList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())

    /**
     *  holds the Form data Key Value configs
     */
    val formDataList = MutableLiveData<ArrayList<KeyValueConfig>>(arrayListOf())
}