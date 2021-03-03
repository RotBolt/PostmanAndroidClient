package io.rotlabs.postmanandroidclient.ui.makeRequest.params

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import io.rotlabs.postmanandroidclient.databinding.FragmentParamsBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.MakeRequestSharedViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueType
import javax.inject.Inject


class ParamsFragment : BaseFragment<FragmentParamsBinding, ParamsViewModel>() {

    @Inject
    lateinit var requestConfigSharedViewModel: MakeRequestSharedViewModel


    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentParamsBinding {
        return FragmentParamsBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        binding.addKeyValueLayout.loadLayout(KeyValueType.QUERY_PARAM, this, childFragmentManager)
    }

    override fun setupObservables() {
        super.setupObservables()
        binding.addKeyValueLayout.keyValueConfigDataHolder.paramList.observe(this, {
            requestConfigSharedViewModel.paramList.postValue(it)
        })
    }


}