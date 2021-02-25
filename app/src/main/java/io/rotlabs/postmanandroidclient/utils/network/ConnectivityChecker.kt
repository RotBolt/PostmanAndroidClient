package io.rotlabs.postmanandroidclient.utils.network

/**
 *  Utility class to check whether device is connected to internet or not
 */

interface ConnectivityChecker {
    fun isConnected(): Boolean
}