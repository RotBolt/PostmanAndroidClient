package io.rotlabs.postmanandroidclient.data.remote

import io.reactivex.Single
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.FormDataContent
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.utils.network.MimeType
import io.rotlabs.postmanandroidclient.utils.network.getRequestOrThrowException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

/**
 *  This class handles making network requests to the user specified url.
 *  Network request could be configured as per configuration provided by user
 */
class RestClient @Inject constructor(private val httpClient: OkHttpClient) {

    companion object {
        const val REQUEST_BODY_NULL_MESSAGE = "Request Body cannot be null"
        const val MALFORMED_URL = "Malformed url"
        const val RECEIVED_RESPONSE_NULL = "Response is null"
    }


    /**
     *  makes the network request call and give back the response wrapped Rx single wrapper
     *  to facilitate easy error handling and thread scheduling
     */
    fun makeRequestCall(
        url: String,
        requestMethod: RequestMethod,
        params: Map<String, String>,
        authInfo: AuthInfo,
        headers: Map<String, String>,
        bodyInfo: BodyInfo
    ): Single<Response> {
        val requestObject =
            makeRequestObject(url, requestMethod, params, authInfo, headers, bodyInfo)
        return Single.create { emitter ->
            try {
                httpClient
                    .newCall(requestObject)
                    .enqueue(
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                emitter.onError(e)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                emitter.onSuccess(response)
                            }

                        })
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }


    /**
     *  generates the request as per the given configuration
     *
     *  @param url : String url where to make the request
     *  @param requestMethod : RequestMethod enum value. Could one of GET,POST,PUT,PATCH_DELETE
     *  @param params: QueryParameters passed in url to make the network request
     *  @param authInfo: AuthInfo determins the type of authentication and apply the authentication to request accordingly
     *  @param headers : Map of network headers used for making networking request
     *  @param bodyInfo: BodyInfo determines the type body sent with request and apply the body to the request
     *
     *  @return @Request
     *
     */

    fun makeRequestObject(
        url: String,
        requestMethod: RequestMethod,
        params: Map<String, String>,
        authInfo: AuthInfo,
        headers: Map<String, String>,
        bodyInfo: BodyInfo
    ): Request {
        val requestBuilder = Request.Builder()

        val queryParams = params.toMutableMap()

        val requestBody = when (bodyInfo) {
            is BodyInfo.RawBody -> bodyInfo.content.toRequestBody(MimeType.TEXT.type.toMediaTypeOrNull())
            is BodyInfo.FormDataBody -> {
                if (bodyInfo.content.isNotEmpty()) {
                    generateFormDataRequestBody(bodyInfo)
                } else {
                    "".toRequestBody()
                }
            }
            is BodyInfo.NoBody -> "".toRequestBody()
        }

        when (requestMethod) {
            RequestMethod.GET -> requestBuilder.get()
            RequestMethod.POST -> requestBuilder.post(requestBody.getRequestOrThrowException("$REQUEST_BODY_NULL_MESSAGE in Post Request"))
            RequestMethod.PUT -> requestBuilder.put(requestBody.getRequestOrThrowException("$REQUEST_BODY_NULL_MESSAGE in Put Request"))
            RequestMethod.PATCH -> requestBuilder.patch(requestBody.getRequestOrThrowException("$REQUEST_BODY_NULL_MESSAGE in Patch Request"))
            RequestMethod.DELETE -> requestBuilder.delete(requestBody.getRequestOrThrowException("$REQUEST_BODY_NULL_MESSAGE in Delete Request"))
        }

        for (header in headers) {
            requestBuilder.addHeader(header.key, header.value)
        }

        when (authInfo) {
            is AuthInfo.NoAuth -> {
                // do nothing
            }

            is AuthInfo.BearerTokenInfo -> {
                requestBuilder.addHeader(
                    Networking.HEADER_AUTHORIZATION,
                    "Bearer ${authInfo.token}"
                )
            }

            is AuthInfo.ApiKeyAuthInfo -> {
                if (authInfo.key.isNotEmpty()) {
                    if (authInfo.isHeader) {
                        requestBuilder.addHeader(authInfo.key, authInfo.value)
                    } else {
                        queryParams[authInfo.key] = authInfo.value
                    }
                }
            }

            is AuthInfo.BasicAuthInfo -> {
                val credentials = Credentials.basic(authInfo.login, authInfo.password)
                requestBuilder.addHeader(Networking.HEADER_AUTHORIZATION, credentials)
            }

        }

        requestBuilder.url(generateHttpUrl(url, queryParams))

        return requestBuilder.build()
    }


    /**
     *
     *  forms the httpurl with given String and query parameters
     *
     *  @return  @HttpUrl
     *  @throws @IllegalStateException if url is malformed or empty
     */
    fun generateHttpUrl(url: String, queryParams: Map<String, String>): HttpUrl {
        val httpUrl = url.toHttpUrlOrNull()


        val httpUrlBuilder =
            httpUrl?.newBuilder() ?: throw IllegalStateException("$MALFORMED_URL: $url")


        for (param in queryParams) {
            httpUrlBuilder.addQueryParameter(param.key, param.value)
        }
        return httpUrlBuilder.build()

    }


    /**
     *  generates Formdata body for the user given Formdata content
     *  @return MultiPartBody
     */

    fun generateFormDataRequestBody(formDataBody: BodyInfo.FormDataBody): MultipartBody {
        val formDataRequestBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        for (formData in formDataBody.content) {
            when (val formDataContent = formData.value) {
                is FormDataContent.FormDataText -> formDataRequestBuilder.addFormDataPart(
                    formData.key,
                    null,
                    formDataContent.content.toRequestBody(MimeType.TEXT.type.toMediaTypeOrNull())
                )

                is FormDataContent.FormDataFile -> {
                    val fileRequestBody =
                        formDataContent.file.asRequestBody(formDataContent.mimeType.type.toMediaTypeOrNull())
                    formDataRequestBuilder.addFormDataPart(
                        formData.key,
                        formDataContent.file.name,
                        fileRequestBody
                    )
                }
            }
        }

        return formDataRequestBuilder.build()
    }

}