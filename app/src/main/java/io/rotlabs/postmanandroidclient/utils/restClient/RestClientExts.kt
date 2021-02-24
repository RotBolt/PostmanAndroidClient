package io.rotlabs.postmanandroidclient.utils.restClient

import okhttp3.HttpUrl
import okhttp3.RequestBody

fun RequestBody?.getRequestOrThrowException(
    errorMessage: String
): RequestBody {
    return this ?: throw IllegalStateException(errorMessage)
}
