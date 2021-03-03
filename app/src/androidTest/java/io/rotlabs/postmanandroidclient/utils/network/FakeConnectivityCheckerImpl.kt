package io.rotlabs.postmanandroidclient.utils.network

import android.content.Context
import io.rotlabs.postmanandroidclient.di.ApplicationContext

class FakeConnectivityCheckerImpl(@ApplicationContext private val appContext: Context) :
    ConnectivityChecker {
    override fun isConnected(): Boolean {
        return true
    }
}