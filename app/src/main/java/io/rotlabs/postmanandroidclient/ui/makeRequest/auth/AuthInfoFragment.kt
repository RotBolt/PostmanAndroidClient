package io.rotlabs.postmanandroidclient.ui.makeRequest.auth

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.data.models.AuthInfo
import io.rotlabs.postmanandroidclient.data.models.AuthType
import io.rotlabs.postmanandroidclient.data.models.RequestMethod
import io.rotlabs.postmanandroidclient.databinding.FragmentAuthInfoBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.MakeRequestSharedViewModel
import io.rotlabs.postmanandroidclient.utils.common.applySearchRxOpeations
import io.rotlabs.postmanandroidclient.utils.common.registerTextChange
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthInfoFragment : BaseFragment<FragmentAuthInfoBinding, AuthInfoViewModel>(),
    AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var makeRequestSharedViewModel: MakeRequestSharedViewModel

    private var currentAuthInfo: AuthInfo = AuthInfo.NoAuth()

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var schedulerProvider: SchedulerProvider


    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAuthInfoBinding {
        return FragmentAuthInfoBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupAuthInfoTypeSpinner()
        setupAuthApiKeyContainer()
        setupAuthTokenContainer()
        setupAuthBasicContainer()
    }

    private fun setupAuthApiKeyContainer() {
        setAuthApiKeySpinner()
        binding.authInfoApiKeyContainer.apply {

            compositeDisposable.add(
                etAuthApiKeyKey.registerTextChange()
                    .applySearchRxOpeations(schedulerProvider)
                    .subscribe(
                        {
                            (currentAuthInfo as AuthInfo.ApiKeyAuthInfo).key = it
                            makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
                        },
                        {
                            showToast(it.message ?: "Oops")
                        }
                    )
            )

            compositeDisposable.add(
                etAuthApiKeyValue.registerTextChange()
                    .applySearchRxOpeations(schedulerProvider)
                    .subscribe(
                        {
                            (currentAuthInfo as AuthInfo.ApiKeyAuthInfo).value = it
                            makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
                        },
                        {
                            showToast(it.message ?: "Oops")
                        }
                    )
            )

        }
    }

    private fun setupAuthTokenContainer() {
        compositeDisposable.add(
            binding.authInfoTokenContainer.etAuthToken.registerTextChange()
                .applySearchRxOpeations(schedulerProvider)
                .subscribe(
                    {
                        (currentAuthInfo as AuthInfo.BearerTokenInfo).token = it
                        makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
                    },
                    {
                        showToast(it.message ?: "Oops")
                    }
                )
        )

    }

    private fun setupAuthBasicContainer() {
        binding.authInfoBasicContainer.apply {

            compositeDisposable.add(
                etAuthBasicUsername.registerTextChange()
                    .applySearchRxOpeations(schedulerProvider)
                    .subscribe(
                        {
                            (currentAuthInfo as AuthInfo.BasicAuthInfo).login = it
                            makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
                        },
                        {
                            showToast(it.message ?: "Oops")
                        }
                    )
            )


            compositeDisposable.add(
                etAuthBasicPassword.registerTextChange()
                    .applySearchRxOpeations(schedulerProvider)
                    .subscribe(
                        {
                            (currentAuthInfo as AuthInfo.BasicAuthInfo).password = it
                            makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
                        },
                        {
                            showToast(it.message ?: "Oops")
                        }
                    )
            )


            showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    etAuthBasicPassword.inputType = InputType.TYPE_CLASS_TEXT
                } else {
                    etAuthBasicPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }
    }

    private fun setupAuthInfoTypeSpinner() {
        val authInfoTypes: Array<String> = EnumSet
            .allOf(AuthType::class.java)
            .map { it.name }
            .toTypedArray()

        val authTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, authInfoTypes)
        authTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.authTypeSpinner.adapter = authTypeAdapter
        binding.authTypeSpinner.onItemSelectedListener = this
        binding.authTypeSpinner.setSelection(0)
    }

    private fun setAuthApiKeySpinner() {
        val types = listOf("Header", "Query Param")
        val typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.authInfoApiKeyContainer.authApiKeySpinner.apply {
            adapter = typeAdapter
            onItemSelectedListener = this@AuthInfoFragment
            setSelection(0)
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.authTypeSpinner -> {
                binding.authTypeSpinner.setSelection(position)
                when (position) {
                    0 -> showAndSelectNoAuth()
                    1 -> showAndSelectAuthApiKey()
                    2 -> showAndSelectAuthBasic()
                    3 -> showAndSelectAuthToken()
                }
            }
            R.id.authApiKeySpinner -> {
                binding.authInfoApiKeyContainer.authApiKeySpinner.setSelection(position)
                when (position) {
                    0 -> {
                        (currentAuthInfo as AuthInfo.ApiKeyAuthInfo).isHeader = true
                    }
                    1 -> {
                        (currentAuthInfo as AuthInfo.ApiKeyAuthInfo).isHeader = false
                    }
                }
                makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
            }
        }
    }

    private fun showAndSelectNoAuth() {
        with(binding) {
            tvNoAuth.isVisible = true
            authInfoApiKeyContainer.root.isVisible = false
            authInfoTokenContainer.root.isVisible = false
            authInfoBasicContainer.root.isVisible = false
        }

        currentAuthInfo = AuthInfo.NoAuth()
        makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
    }

    private fun showAndSelectAuthApiKey() {
        with(binding) {
            tvNoAuth.isVisible = false
            authInfoApiKeyContainer.root.isVisible = true
            authInfoTokenContainer.root.isVisible = false
            authInfoBasicContainer.root.isVisible = false
        }

        val key = binding.authInfoApiKeyContainer.etAuthApiKeyKey.text?.toString() ?: ""
        val value = binding.authInfoApiKeyContainer.etAuthApiKeyValue.text?.toString() ?: ""

        val isHeader = binding.authInfoApiKeyContainer.authApiKeySpinner.selectedItemPosition == 0
        currentAuthInfo = AuthInfo.ApiKeyAuthInfo(key, value, isHeader)
        makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
    }

    private fun showAndSelectAuthToken() {
        with(binding) {
            tvNoAuth.isVisible = false
            authInfoApiKeyContainer.root.isVisible = false
            authInfoTokenContainer.root.isVisible = true
            authInfoBasicContainer.root.isVisible = false
        }

        val token = binding.authInfoTokenContainer.etAuthToken.text?.toString() ?: ""

        currentAuthInfo = AuthInfo.BearerTokenInfo(token)
        makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
    }

    private fun showAndSelectAuthBasic() {
        with(binding) {
            tvNoAuth.isVisible = false
            authInfoApiKeyContainer.root.isVisible = false
            authInfoTokenContainer.root.isVisible = false
            authInfoBasicContainer.root.isVisible = true
        }

        val username = binding.authInfoBasicContainer.etAuthBasicUsername.text?.toString() ?: ""
        val password = binding.authInfoBasicContainer.etAuthBasicPassword.text?.toString() ?: ""

        currentAuthInfo = AuthInfo.BasicAuthInfo(username, password)
        makeRequestSharedViewModel.authInfo.postValue(currentAuthInfo)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
}