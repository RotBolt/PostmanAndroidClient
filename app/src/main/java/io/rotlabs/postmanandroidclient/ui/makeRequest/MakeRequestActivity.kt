package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.databinding.ActivityMakeRequestBinding
import io.rotlabs.postmanandroidclient.di.component.ActivityComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseActivity
import io.rotlabs.postmanandroidclient.utils.common.registerTextChange
import java.util.*
import kotlin.random.Random

class MakeRequestActivity : BaseActivity<ActivityMakeRequestBinding, MakeRequestViewModel>(),
    View.OnFocusChangeListener, AdapterView.OnItemSelectedListener, View.OnClickListener {


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
    private var currentQueryParams = mutableMapOf<String, String>()

    /**
     *  holds the headers required to make the current request
     */
    private var currentHeaders = mutableMapOf<String, String>()

    override fun provideActivityBinding(): ActivityMakeRequestBinding {
        return ActivityMakeRequestBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(buildComponent: ActivityComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupRequestUrlEditText()
        setupRequestTypeSpinner()

        binding.btnSend.setOnClickListener(this)
        binding.btnResponse.setOnClickListener(this)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.etRequestUrl -> onRequestUrlFocusChange(hasFocus)
        }
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
        binding.etRequestUrl.registerTextChange { s, _, _, _ ->
            currentRequestUrl = s.toString()
        }
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
            }
            R.id.btnResponse -> {
                // open bottom sheet
            }
        }
    }
}