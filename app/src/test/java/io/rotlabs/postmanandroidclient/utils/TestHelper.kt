package io.rotlabs.postmanandroidclient.utils

import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.FormDataContent
import io.rotlabs.postmanandroidclient.utils.network.MimeType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.File

class TestHelper {

    fun getTestUrl() = "http://test.com"

    fun getTestHeaders() = mapOf(
        "Accept" to "*/*",
        "Connection" to "keep-alive"
    )

    fun getTestParams() = mapOf(
        "filter" to "book"
    )

    fun getTestLogin() = "username"

    fun getTestPassword() = "password"

    fun getTestToken() =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE"

    fun getTestApiKey() = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ"

    fun getTestFormDataBody(): BodyInfo.FormDataBody {

        val testFileUrl = (this::class as Any).javaClass.classLoader?.getResource("testFile.txt")

        val testFile = testFileUrl?.let { File(it.toURI()) }

        val formDataContent = mutableMapOf<String, FormDataContent>(
            "id" to FormDataContent.FormDataText("testId")
        )

        testFile?.let { file ->
            formDataContent.put("file", FormDataContent.FormDataFile(file, MimeType.TEXT))
        }
        return BodyInfo.FormDataBody(formDataContent)
    }

    fun getTestResponseBody() = "{\"test\":\"json\"}"


    fun getTestSuccessResponse(request: Request): Response {
        val responseBuilder = Response.Builder()
        responseBuilder.request(request)
        responseBuilder.protocol(Protocol.HTTP_2)
        responseBuilder.message("Success")
        getTestHeaders().forEach { (key, value) ->
            responseBuilder.addHeader(key, value)
        }
        responseBuilder.code(200)
        responseBuilder.body(getTestResponseBody().toResponseBody(MimeType.TEXT.type.toMediaTypeOrNull()))
        return responseBuilder.build()
    }

    fun getTestFailedResponse(request: Request): Response {
        val responseBuilder = Response.Builder()
        responseBuilder.code(404)
        responseBuilder.request(request)
        responseBuilder.protocol(Protocol.HTTP_2)
        responseBuilder.message("Not Found")
        return responseBuilder.build()
    }
}