package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.FragmentAddKeyValueBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseBottomSheetFragment
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
    lateinit var keyValueConfigDataHolder: KeyValueConfigDataHolder


    private var isNew: Boolean = true

    private var keyValueConfig: KeyValueConfig? = null

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
        keyValueConfig = arguments?.getSerializable(ARG_KEY_VALUE_CONFIG) as KeyValueConfig?
        isNew = keyValueConfig == null
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
                saveKeyValuePairAndUpdateList(
                    keyValueConfigDataHolder.headerList,
                    key,
                    value,
                    description
                )
            }
            KeyValueType.QUERY_PARAM -> {
                saveKeyValuePairAndUpdateList(
                    keyValueConfigDataHolder.paramList,
                    key,
                    value,
                    description
                )
            }
        }

        dismiss()
        // TODO call viewModel to save the config in local store

    }

    private fun saveKeyValuePairAndUpdateList(
        liveList: MutableLiveData<ArrayList<KeyValueConfig>>,
        key: String,
        value: String,
        description: String
    ) {
        if (isNew) {
            addKeyValue(
                liveList.value,
                key,
                value,
                description
            )
        } else {
            updateKeyValue(
                liveList.value,
                key,
                value,
                description
            )
        }
        liveList.postValue(liveList.value)
    }

    private fun addKeyValue(
        list: ArrayList<KeyValueConfig>?,
        key: String,
        value: String,
        description: String
    ) {
        list?.add(KeyValueConfig(key, value, description, true))
    }

    private fun updateKeyValue(
        list: ArrayList<KeyValueConfig>?,
        key: String,
        value: String,
        description: String
    ) {
        keyValueConfig?.let { keyValueConfig ->
            val oldKeyValueConfig = list?.find {
                it.key == keyValueConfig.key && it.value == keyValueConfig.value && it.description == keyValueConfig.description
            }
            oldKeyValueConfig?.let {
                it.key = key
                it.value = value
                it.description = description
            }
        }
    }

    private fun getKeyValueType(): String {
        return arguments?.getString(ARG_KEY_VALUE_TYPE)!!
    }
}