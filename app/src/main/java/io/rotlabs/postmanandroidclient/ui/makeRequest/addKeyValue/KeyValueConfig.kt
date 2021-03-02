package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import java.io.Serializable


/**
 *  Holds the key value (Query Param or Header) along with description
 *  Helper Class to properly hold data and show in UI layer
 */
data class KeyValueConfig(
    var key: String,
    var value: String,
    var description: String,
    var toInclude: Boolean
) : Serializable