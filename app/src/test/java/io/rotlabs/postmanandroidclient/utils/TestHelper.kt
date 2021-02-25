package io.rotlabs.postmanandroidclient.utils

import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.FormDataContent
import io.rotlabs.postmanandroidclient.utils.network.MimeType
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
}