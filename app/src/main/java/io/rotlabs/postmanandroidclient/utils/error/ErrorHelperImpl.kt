package io.rotlabs.postmanandroidclient.utils.error

import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

class ErrorHelperImpl : ErrorHelper {

    override fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is ConnectException -> ErrorMessage.NOT_ABLE_TO_CONNECT
            is SocketTimeoutException -> ErrorMessage.TIME_OUT_OCCURRED
            is OfflineException -> ErrorMessage.OFFLINE
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> ErrorMessage.BAD_REQUEST
                    HttpURLConnection.HTTP_BAD_GATEWAY -> ErrorMessage.INTERNAL_SERVER_ERROR
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorMessage.NOT_FOUND
                    HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorMessage.UNAUTHORIZED
                    else -> ErrorMessage.DEFAULT_ERROR_MESSAGE
                }
            }
            else -> {
                if (throwable?.message?.contains("Unable to resolve host") == true) {
                    ErrorMessage.UNKNOWN_HOST
                } else {
                    throwable?.message ?: ErrorMessage.DEFAULT_ERROR_MESSAGE
                }
            }
        }
    }

}