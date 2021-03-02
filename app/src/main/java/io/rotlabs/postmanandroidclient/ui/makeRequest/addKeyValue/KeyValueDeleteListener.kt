package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

/**
 *  Notifies when the key value is deleted
 */
interface KeyValueDeleteListener {

    fun onDeleteKeyValueConfig(keyValueConfig: KeyValueConfig, position: Int)
}