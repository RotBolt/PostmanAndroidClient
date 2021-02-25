package io.rotlabs.postmanandroidclient.utils.error

import java.lang.RuntimeException

class OfflineException(errorMessage: String = ErrorMessage.OFFLINE) :
    RuntimeException(errorMessage)