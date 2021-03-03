package io.rotlabs.postmanandroidclient.ui.makeRequest.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import io.rotlabs.postmanandroidclient.databinding.FragmentHeadersBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.MakeRequestSharedViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueType
import javax.inject.Inject


class HeaderFragment : BaseFragment<FragmentHeadersBinding, HeaderViewModel>() {


    @Inject
    lateinit var requestConfigSharedViewModel: MakeRequestSharedViewModel

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHeadersBinding {
        return FragmentHeadersBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        binding.addKeyValueLayout.loadLayout(KeyValueType.HEADER, this, childFragmentManager)
    }

    override fun setupObservables() {
        super.setupObservables()
        binding.addKeyValueLayout.keyValueConfigDataHolder.headerList.observe(this, {
            requestConfigSharedViewModel.headerList.postValue(it)
        })
    }


}