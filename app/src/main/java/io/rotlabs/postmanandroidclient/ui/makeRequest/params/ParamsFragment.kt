package io.rotlabs.postmanandroidclient.ui.makeRequest.params

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.FragmentParamsHeadersBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.AddKeyValueBottomSheet
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueAddRemoveListener


class ParamsFragment : BaseFragment<FragmentParamsHeadersBinding, ParamsViewModel>(),
    View.OnClickListener, KeyValueAddRemoveListener {


    private lateinit var keyValueAddRemoveListener: KeyValueAddRemoveListener

    fun setKeyValueAddRemoveListener(listener: KeyValueAddRemoveListener) {
        this.keyValueAddRemoveListener = listener
    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParamsHeadersBinding {
        return FragmentParamsHeadersBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupBtnAddKeyValueConfig()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddKeyValueConfig -> {
                showAddKeyValueBottomSheet()
            }
        }
    }

    private fun setupBtnAddKeyValueConfig() {
        binding.btnAddKeyValueConfig.text = "Add Params"
        binding.btnAddKeyValueConfig.setOnClickListener(this)
    }

    private fun showAddKeyValueBottomSheet() {
        val fragment = AddKeyValueBottomSheet.newInstance(true)
        fragment.setKeyValueAddRemoveListener(this)
        fragment.show(childFragmentManager, AddKeyValueBottomSheet.TAG)
    }

    override fun addOrRemoveKeyValue(
        key: String,
        value: String,
        description: String,
        toAdd: Boolean
    ) {
        // add item to adapter
        keyValueAddRemoveListener.addOrRemoveKeyValue(key, value, description, toAdd)
    }

}