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

class AddKeyValueBottomSheet :
    BaseBottomSheetFragment<FragmentAddKeyValueBinding, AddKeyValueViewModel>(),
    View.OnClickListener {

    companion object {
        const val TAG = "AddKeyValueBottomSheet"
        private const val ARG_IS_NEW = "isNew"
        fun newInstance(isNew: Boolean): AddKeyValueBottomSheet {
            val args = Bundle()
            args.putBoolean(ARG_IS_NEW, isNew)
            val fragment = AddKeyValueBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var keyValueAddRemoveListener: KeyValueAddRemoveListener

    fun setKeyValueAddRemoveListener(listener: KeyValueAddRemoveListener) {
        this.keyValueAddRemoveListener = listener
    }

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

        val isNew = arguments?.getBoolean(ARG_IS_NEW) ?: true
        if (isNew) {
            binding.btnDeleteKeyValue.isVisible = false
        } else {
            binding.btnSaveKeyValue.isVisible = false
        }

        binding.btnDeleteKeyValue.setOnClickListener(this)
        binding.btnSaveKeyValue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSaveKeyValue -> saveKeyValuePair()
            R.id.btnDeleteKeyValue -> deleteKeyValuePair()
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

        keyValueAddRemoveListener.addOrRemoveKeyValue(key, value, description, true)

        // TODO call viewModel to save the config in local store

    }

    private fun deleteKeyValuePair() {
        // TODO use the loaded key value pair to delete from local
    }
}