package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

interface KeyValueAddRemoveListener {
    fun addOrRemoveKeyValue(key: String, value: String, description: String, toAdd: Boolean)
}