package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

/**
 *  Notifies when the Key Value (Query Params or Header) is included or excluded
 */
interface KeyValueIncludeChangeListener {

    fun onKeyValueIncludeChange(keyValueConfig: KeyValueConfig, position: Int)
}