package io.rotlabs.postmanandroidclient.data.models

/**
 *  Model class to wrap the AuthData and type of authentication which is applied in Request
 */

sealed class AuthInfo(authType: AuthType) {
    class NoAuth : AuthInfo(AuthType.NO_AUTH)

    data class ApiKeyAuthInfo(val key: String, val value: String, val isHeader: Boolean) :
        AuthInfo(AuthType.API_KEY)

    data class BasicAuthInfo(val login: String, val password: String) :
        AuthInfo(AuthType.BASIC_AUTH)

    data class BearerTokenInfo(val token: String) : AuthInfo(AuthType.BEARER_TOKEN)
}

enum class AuthType {
    NO_AUTH,
    API_KEY,
    BASIC_AUTH,
    BEARER_TOKEN
}