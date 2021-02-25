package io.rotlabs.postmanandroidclient.utils.network

import okhttp3.RequestBody

fun RequestBody?.getRequestOrThrowException(
    errorMessage: String
): RequestBody {
    return this ?: throw IllegalStateException(errorMessage)
}
