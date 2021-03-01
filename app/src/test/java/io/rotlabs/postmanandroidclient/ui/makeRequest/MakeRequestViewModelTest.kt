package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.data.remote.RestClient
import io.rotlabs.postmanandroidclient.utils.TestHelper
import io.rotlabs.postmanandroidclient.utils.TestSchedulerProvider
import io.rotlabs.postmanandroidclient.utils.error.ErrorHelperImpl
import io.rotlabs.postmanandroidclient.utils.network.ConnectivityCheckerImpl
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MakeRequestViewModelTest {

    /**
     *  Rule to swaps the background executor to synchronous task executor to run the tests
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var restClient: RestClient

    @Mock
    lateinit var errorHelper: ErrorHelperImpl

    @Mock
    lateinit var connectivityChecker: ConnectivityCheckerImpl

    @Mock
    lateinit var progressObserver: Observer<Boolean>

    @Mock
    lateinit var responseObserver: Observer<Response>

    @Mock
    lateinit var malformedUrlObserver: Observer<String>

    private lateinit var testScheduler: TestScheduler

    private lateinit var testHelper: TestHelper

    private lateinit var makeRequestViewModel: MakeRequestViewModel

    @Before
    fun setup() {
        testHelper = TestHelper()
        testScheduler = TestScheduler()
        makeRequestViewModel = MakeRequestViewModel(
            restClient,
            TestSchedulerProvider(testScheduler),
            CompositeDisposable(),
            errorHelper,
            connectivityChecker
        )

        makeRequestViewModel.apply {
            malformedUrl.observeForever(malformedUrlObserver)
            response.observeForever(responseObserver)
            progress.observeForever(progressObserver)
        }
    }

    @Test
    fun given_successful_response_should_observe_response() {
        val request: Request = mock(Request::class.java)

        val testResponse: Response = testHelper.getTestSuccessResponse(request)

        val testUrl = testHelper.getTestUrl()
        val getMethod = RequestMethod.GET
        val queryParams = testHelper.getTestParams()
        val authInfo = AuthInfo.NoAuth()
        val headers = testHelper.getTestHeaders()
        val bodyInfo = BodyInfo.NoBody()

        doReturn(Single.just(testResponse))
            .`when`(restClient)
            .makeRequestCall(
                testUrl,
                getMethod,
                queryParams,
                authInfo,
                headers,
                bodyInfo
            )

        makeRequestViewModel.makeRequest(
            testUrl,
            getMethod,
            queryParams,
            authInfo,
            headers,
            bodyInfo
        )

        testScheduler.triggerActions()

        verify(progressObserver).onChanged(false)
        verify(progressObserver).onChanged(true)

        verify(responseObserver).onChanged(testResponse)

        assert(makeRequestViewModel.response.value == testResponse)
        assert(makeRequestViewModel.response.value?.code == 200)
    }


    @Test
    fun given_failed_response_should_observe_response() {
        val request: Request = mock(Request::class.java)

        val testResponse: Response = testHelper.getTestFailedResponse(request)

        val testUrl = testHelper.getTestUrl()
        val getMethod = RequestMethod.GET
        val queryParams = testHelper.getTestParams()
        val authInfo = AuthInfo.NoAuth()
        val headers = testHelper.getTestHeaders()
        val bodyInfo = BodyInfo.NoBody()

        doReturn(Single.just(testResponse))
            .`when`(restClient)
            .makeRequestCall(
                testUrl,
                getMethod,
                queryParams,
                authInfo,
                headers,
                bodyInfo
            )

        makeRequestViewModel.makeRequest(
            testUrl,
            getMethod,
            queryParams,
            authInfo,
            headers,
            bodyInfo
        )

        testScheduler.triggerActions()

        verify(progressObserver).onChanged(false)
        verify(progressObserver).onChanged(true)

        verify(responseObserver).onChanged(testResponse)

        assert(makeRequestViewModel.response.value == testResponse)
        assert(makeRequestViewModel.response.value?.code == 404)
    }

    @Test
    fun given_invalid_url_should_observe_malformedUrl() {

        val testUrl = "xyx"
        val getMethod = RequestMethod.GET
        val queryParams = testHelper.getTestParams()
        val authInfo = AuthInfo.NoAuth()
        val headers = testHelper.getTestHeaders()
        val bodyInfo = BodyInfo.NoBody()

        makeRequestViewModel.makeRequest(
            testUrl,
            getMethod,
            queryParams,
            authInfo,
            headers,
            bodyInfo
        )

        verify(progressObserver).onChanged(false)
        verify(progressObserver).onChanged(true)

        verify(malformedUrlObserver).onChanged(RestClient.MALFORMED_URL)

        assert(makeRequestViewModel.malformedUrl.value == RestClient.MALFORMED_URL)
    }


    @Test
    fun given_valid_url_isValidUrl_method_should_pass() {
        assert(makeRequestViewModel.isValidURL(testHelper.getTestUrl()))
    }

    @Test
    fun given_invalid_url_isValidUrl_method_should_fail() {
        assert(!makeRequestViewModel.isValidURL("xyx"))
    }


    @After
    fun tearDown() {
        makeRequestViewModel.apply {
            progress.removeObserver(progressObserver)
            malformedUrl.removeObserver(malformedUrlObserver)
            response.removeObserver(responseObserver)
        }
    }
}