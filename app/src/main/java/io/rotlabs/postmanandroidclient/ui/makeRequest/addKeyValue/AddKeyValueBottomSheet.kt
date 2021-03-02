package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.FragmentAddKeyValueBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseBottomSheetFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.RequestConfigSharedViewModel
import javax.inject.Inject

class AddKeyValueBottomSheet :
    BaseBottomSheetFragment<FragmentAddKeyValueBinding, AddKeyValueViewModel>(),
    View.OnClickListener {

    companion object {
        const val TAG = "AddKeyValueBottomSheet"
        private const val ARG_KEY_VALUE_CONFIG = "keyValueConfig"
        private const val ARG_KEY_VALUE_TYPE = "keyValueType"
        fun newInstance(
            keyValueType: String,
            keyValueConfig: KeyValueConfig?
        ): AddKeyValueBottomSheet {
            val args = Bundle()
            args.putString(ARG_KEY_VALUE_TYPE, keyValueType)
            keyValueConfig?.let {
                args.putSerializable(ARG_KEY_VALUE_CONFIG, it)
            }
            val fragment = AddKeyValueBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var requestConfigSharedViewModel: RequestConfigSharedViewModel


    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddKeyValueBinding {
        return FragmentAddKeyValueBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        binding.tvAddConfigHeading.text = getKeyValueType()
        binding.btnSaveKeyValue.setOnClickListener(this)
        setupKeyValueConfig()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSaveKeyValue -> saveKeyValuePair()
        }
    }

    /**
     *  setup the initial values if any to display to the user
     */
    private fun setupKeyValueConfig() {
        val keyValueConfig = arguments?.getSerializable(ARG_KEY_VALUE_CONFIG) as KeyValueConfig?
        keyValueConfig?.let { config ->
            binding.apply {
                etKey.setText(config.key)
                etValue.setText(config.value)
                etDescription.setText(config.description)
            }
        }
    }

    private fun saveKeyValuePair() {
        val key = binding.etKey.text?.toString() ?: ""
        val value = binding.etValue.text?.toString() ?: ""
        val description = binding.etDescription.text?.toString() ?: ""

        if (key.isEmpty()) {
            binding.etKey.error = "Key cannot be empty"
            return
        }

        when (getKeyValueType()) {
            KeyValueType.HEADER -> {
                requestConfigSharedViewModel.headerList.value?.add(
                    KeyValueConfig(
                        key,
                        value,
                        description,
                        true
                    )
                )
                requestConfigSharedViewModel.headerList.postValue(requestConfigSharedViewModel.headerList.value)
            }
            KeyValueType.QUERY_PARAM -> {
                requestConfigSharedViewModel.paramList.value?.add(
                    KeyValueConfig(
                        key,
                        value,
                        description,
                        true
                    )
                )
                requestConfigSharedViewModel.paramList.postValue(requestConfigSharedViewModel.paramList.value)
            }
        }

        dismiss()
        // TODO call viewModel to save the config in local store

    }

    private fun getKeyValueType(): String {
        return arguments?.getString(ARG_KEY_VALUE_TYPE)!!
    }
}