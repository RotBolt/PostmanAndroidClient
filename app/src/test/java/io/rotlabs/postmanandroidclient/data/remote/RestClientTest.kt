package io.rotlabs.postmanandroidclient.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.utils.TestHelper
import io.rotlabs.postmanandroidclient.utils.rx.TestSchedulerProvider
import io.rotlabs.postmanandroidclient.utils.network.MimeType
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class RestClientTest {

    /**
     *  Rule to swaps the background executor to synchronous task executor to run the tests
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var httpClient: OkHttpClient

    private lateinit var restClient: RestClient

    private lateinit var testHelper: TestHelper

    private lateinit var testSchedulerProvider: TestSchedulerProvider

    private lateinit var testScheduler: TestScheduler

    private lateinit var server: MockWebServer

    private lateinit var testUrl: HttpUrl

    @Before
    fun setup() {

        httpClient = OkHttpClient()
        restClient = RestClient(httpClient)
        testHelper = TestHelper()
        testScheduler = TestScheduler()
        testSchedulerProvider = TestSchedulerProvider(testScheduler)

        server = MockWebServer()
        server.start()
        testUrl = server.url("/")
    }

    @Test
    fun given_PostMethod_should_make_Post_Request() {
        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.POST,
            testHelper.getTestParams(),
            AuthInfo.NoAuth(),
            testHelper.getTestHeaders(),
            BodyInfo.RawBody("This is test content")
        )

        assert(request.method == "POST")
    }

    @Test
    fun given_PatchMethod_should_make_Post_Request() {
        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.PATCH,
            testHelper.getTestParams(),
            AuthInfo.NoAuth(),
            testHelper.getTestHeaders(),
            BodyInfo.RawBody("This is test content")
        )

        assert(request.method == "PATCH")
    }

    @Test
    fun given_PutMethod_should_make_Post_Request() {
        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.PUT,
            testHelper.getTestParams(),
            AuthInfo.NoAuth(),
            testHelper.getTestHeaders(),
            BodyInfo.RawBody("This is test content")
        )

        assert(request.method == "PUT")
    }

    @Test
    fun given_DeleteMethod_should_make_Post_Request() {
        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.DELETE,
            testHelper.getTestParams(),
            AuthInfo.NoAuth(),
            testHelper.getTestHeaders(),
            BodyInfo.RawBody("This is test content")
        )

        assert(request.method == "DELETE")
    }

    @Test
    fun given_GetMethod_should_make_Post_Request() {
        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.GET,
            testHelper.getTestParams(),
            AuthInfo.NoAuth(),
            testHelper.getTestHeaders(),
            BodyInfo.NoBody()
        )

        assert(request.method == "GET")
    }

    @Test
    fun given_AuthInfoBasic_should_contain_basic_credential_in_header() {
        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.GET,
            testHelper.getTestParams(),
            AuthInfo.BasicAuthInfo(testHelper.getTestLogin(), testHelper.getTestPassword()),
            testHelper.getTestHeaders(),
            BodyInfo.NoBody()
        )

        val authHeaderCredentials = Credentials.basic(
            testHelper.getTestLogin(),
            testHelper.getTestPassword()
        )

        assert(request.headers[Networking.HEADER_AUTHORIZATION] == authHeaderCredentials)
    }


    @Test
    fun given_AuthInfoBearerToken_should_contain_bearer_token_in_header() {

        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.GET,
            testHelper.getTestParams(),
            AuthInfo.BearerTokenInfo(testHelper.getTestToken()),
            testHelper.getTestHeaders(),
            BodyInfo.NoBody()
        )

        assert(request.headers[Networking.HEADER_AUTHORIZATION] == "Bearer ${testHelper.getTestToken()}")
    }

    @Test
    fun given_AuthInfoApiKey_should_contain_api_key_in_header() {

        val request = restClient.makeRequestObject(
            testUrl.toString(),
            RequestMethod.GET,
            testHelper.getTestParams(),
            AuthInfo.ApiKeyAuthInfo(Networking.HEADER_API_KEY, testHelper.getTestApiKey(), true),
            testHelper.getTestHeaders(),
            BodyInfo.NoBody()
        )


        assert(request.headers[Networking.HEADER_API_KEY] == testHelper.getTestApiKey())
    }


    @Test
    fun given_NullUrl_should_throw_illegalStateException() {
        val emptyUrl = ""
        try {
            restClient.generateHttpUrl(emptyUrl, testHelper.getTestParams())
        } catch (e: IllegalStateException) {
            assert(e.message == "${RestClient.MALFORMED_URL}: $emptyUrl")
        }
    }

    @Test
    fun given_ValidUrl_should_return_httpUrl() {
        val httpUrl =
            restClient.generateHttpUrl(testUrl.toString(), testHelper.getTestParams())
        assert(httpUrl.host == testUrl.host)
        assert(httpUrl.queryParameterNames.size == testHelper.getTestParams().size)
        assert(httpUrl.queryParameter("filter") == "book")
    }


    @Test
    fun given_NoBody_in_Post_method_should_throw_illegalStateException() {
        try {
            restClient.makeRequestObject(
                testUrl.toString(),
                RequestMethod.POST,
                testHelper.getTestParams(),
                AuthInfo.NoAuth(),
                testHelper.getTestHeaders(),
                BodyInfo.NoBody()
            )

        } catch (e: IllegalStateException) {
            assert(e.message == "${RestClient.REQUEST_BODY_NULL_MESSAGE} in Post Request")
        }
    }

    @Test
    fun given_NoBody_in_Put_method_should_throw_illegalStateException() {
        try {
            restClient.makeRequestObject(
                testUrl.toString(),
                RequestMethod.PUT,
                testHelper.getTestParams(),
                AuthInfo.NoAuth(),
                testHelper.getTestHeaders(),
                BodyInfo.NoBody()
            )

        } catch (e: IllegalStateException) {
            assert(e.message == "${RestClient.REQUEST_BODY_NULL_MESSAGE} in Put Request")
        }
    }

    @Test
    fun given_NoBody_in_Patch_method_should_throw_illegalStateException() {
        try {
            restClient.makeRequestObject(
                testUrl.toString(),
                RequestMethod.PATCH,
                testHelper.getTestParams(),
                AuthInfo.NoAuth(),
                testHelper.getTestHeaders(),
                BodyInfo.NoBody()
            )

        } catch (e: IllegalStateException) {
            assert(e.message == "${RestClient.REQUEST_BODY_NULL_MESSAGE} in Patch Request")
        }
    }

    @Test
    fun given_NoBody_in_Delete_method_should_throw_illegalStateException() {
        try {
            restClient.makeRequestObject(
                testUrl.toString(),
                RequestMethod.DELETE,
                testHelper.getTestParams(),
                AuthInfo.NoAuth(),
                testHelper.getTestHeaders(),
                BodyInfo.NoBody()
            )

        } catch (e: IllegalStateException) {
            assert(e.message == "${RestClient.REQUEST_BODY_NULL_MESSAGE} in Delete Request")
        }
    }

    @Test
    fun given_FormDataText_requestBody_should_contain_formDataText() {

        val testFormData = testHelper.getTestFormDataBody()
        val requestBodyInfo =
            restClient.generateFormDataRequestBody(testFormData)

        assert(requestBodyInfo.parts.size == testFormData.content.size)

        assert(requestBodyInfo.part(0).body.contentType().toString().contains(MimeType.TEXT.type))
        assert(requestBodyInfo.part(1).body.contentType().toString().contains(MimeType.TEXT.type))

    }

    @Test
    fun given_valid_request_and_valid_response_should_make_request_call_and_receive_response() {

        server.enqueue(
            MockResponse().setResponseCode(200).setBody(testHelper.getTestResponseBody())
        )

        val responseObserver: TestObserver<Response> = restClient.makeRequestCall(
            testUrl.toString(),
            RequestMethod.GET,
            testHelper.getTestParams(),
            AuthInfo.NoAuth(),
            testHelper.getTestHeaders(),
            BodyInfo.NoBody()
        ).test()

        server.takeRequest()

        responseObserver.awaitDone(3, TimeUnit.SECONDS)
        responseObserver.assertComplete()
            .assertValueCount(1)
            .assertValueAt(0) { response ->
                val body = response.body
                body != null && body.string() == testHelper.getTestResponseBody()
            }

    }

}