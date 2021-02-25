package io.rotlabs.postmanandroidclient.utils.error

interface ErrorHelper {

    fun getErrorMessage(throwable: Throwable?): String
}