package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

data class KeyValueConfig(
    var key: String,
    var value: String,
    var description:String,
    var toInclude: Boolean
)