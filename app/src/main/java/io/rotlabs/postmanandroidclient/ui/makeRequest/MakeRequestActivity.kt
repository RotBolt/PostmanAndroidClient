package io.rotlabs.postmanandroidclient.ui.makeRequest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.databinding.ActivityMakeRequestBinding
import io.rotlabs.postmanandroidclient.di.component.ActivityComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseActivity
import io.rotlabs.postmanandroidclient.utils.common.Toaster
import io.rotlabs.postmanandroidclient.utils.common.applySearchRxOpeations
import io.rotlabs.postmanandroidclient.utils.common.hideSoftKeyboard
import io.rotlabs.postmanandroidclient.utils.common.registerTextChange
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MakeRequestActivity : BaseActivity<ActivityMakeRequestBinding, MakeRequestViewModel>(),
    View.OnFocusChangeListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    @Inject
    lateinit var makeRequestSharedViewModel: MakeRequestSharedViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    /**
     *  holds the current selected Request Method to be used
     *  accepted values are from RequestMethod
     *  @see io.rotlabs.postmanandroidclient.data.models.RequestMethod
     */
    private var currentRequestMethod: RequestMethod = RequestMethod.GET

    /**
     *  holds the current selected AuthInfo to be used
     *  accepted values are from AuthInfo
     *  @see io.rotlabs.postmanandroidclient.data.models.AuthInfo
     */
    private var currentAuthInfo: AuthInfo = AuthInfo.NoAuth()

    /**
     *  holds the current selected BodyInfo to be used
     *  accepted values are from BodyInfo
     *  @see io.rotlabs.postmanandroidclient.data.models.BodyInfo
     */
    private var currentBodyInfo: BodyInfo = BodyInfo.NoBody()

    /**
     *  holds the Request url which user types in edit text box
     */
    private var currentRequestUrl: String = ""

    /**
     *  holds the query params required to make the current request
     */
    private val currentQueryParams = mutableMapOf<String, String>()

    /**
     *  holds the headers required to make the current request
     */
    private val currentHeaders = mutableMapOf<String, String>()

    override fun provideActivityBinding(): ActivityMakeRequestBinding {
        return ActivityMakeRequestBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(buildComponent: ActivityComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupRequestUrlEditText()
        setupRequestTypeSpinner()
        setupRequestConfigTabs()

        binding.btnSend.setOnClickListener(this)
        binding.btnResponse.setOnClickListener(this)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.etRequestUrl -> onRequestUrlFocusChange(hasFocus)
        }
    }

    override fun setupObservables() {
        super.setupObservables()

        viewModel.response.observe(this, {
            makeRequestSharedViewModel.response.postValue(it)
        })

        makeRequestSharedViewModel.paramList.observe(this, { list ->
            currentQueryParams.clear()
            list.forEach {
                if (it.toInclude) {
                    currentQueryParams[it.key] = it.value
                }
            }
        })

        makeRequestSharedViewModel.headerList.observe(this, { list ->
            currentHeaders.clear()
            list.forEach {
                if (it.toInclude) {
                    currentHeaders[it.key] = it.value
                }
            }
        })

        makeRequestSharedViewModel.authInfo.observe(this, {
            currentAuthInfo = it
        })

        makeRequestSharedViewModel.bodyInfo.observe(this, {
            currentBodyInfo = it
        })

        viewModel.malformedUrl.observe(this, {
            showToast(it)
        })
    }

    private fun setupRequestConfigTabs() {
        binding.requestConfigTabLayout.requestConfigPager.adapter = RequestConfigsAdapter(this)
        TabLayoutMediator(
            binding.requestConfigTabLayout.requestConfigTabContainer,
            binding.requestConfigTabLayout.requestConfigPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Params"
                1 -> tab.text = "Authorization"
                2 -> tab.text = "Headers"
                3 -> tab.text = "Body"
            }
        }.attach()
    }

    /**
     *  To make changes in RequestUrlContainer view when RequestUrlEditText gets focus
     */
    private fun onRequestUrlFocusChange(hasFocus: Boolean) {
        if (hasFocus) {
            binding.requestUrlContainer.background =
                ContextCompat.getDrawable(this, R.drawable.text_field_bg_focused)
        } else {
            binding.requestUrlContainer.background =
                ContextCompat.getDrawable(this, R.drawable.text_field_bg_unfocused)
        }
    }

    /**
     *  Set up Request Method spinner.
     *  To fill request method choices and set up itemClickListener
     */
    private fun setupRequestTypeSpinner() {
        val requestTypes: Array<String> = EnumSet
            .allOf(RequestMethod::class.java)
            .map { it.name }
            .toTypedArray()

        val requestTypeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, requestTypes)
        requestTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.requestTypeSpinner.adapter = requestTypeAdapter
        binding.requestTypeSpinner.onItemSelectedListener = this
        binding.requestTypeSpinner.setSelection(0)

    }

    private fun setupRequestUrlEditText() {
        binding.etRequestUrl.onFocusChangeListener = this
        compositeDisposable.add(
            binding.etRequestUrl.registerTextChange()
                .applySearchRxOpeations(schedulerProvider)
                .subscribe(
                    {
                        currentRequestUrl = it
                    },
                    {
                        showToast(it.message ?: "Oops")
                    }, {
                        hideSoftKeyboard()
                        binding.etRequestUrl.clearFocus()
                    }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.requestTypeSpinner.setSelection(position)

        when (position) {
            0 -> currentRequestMethod = RequestMethod.GET
            1 -> currentRequestMethod = RequestMethod.POST
            2 -> currentRequestMethod = RequestMethod.PUT
            3 -> currentRequestMethod = RequestMethod.PATCH
            4 -> currentRequestMethod = RequestMethod.DELETE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSend -> {
                viewModel.makeRequest(
                    currentRequestUrl,
                    currentRequestMethod,
                    currentQueryParams,
                    currentAuthInfo,
                    currentHeaders,
                    currentBodyInfo
                )
                // TODO open Response Bottom Sheet
            }
            R.id.btnResponse -> {
                // TODO open bottom sheet
            }
        }
    }
}