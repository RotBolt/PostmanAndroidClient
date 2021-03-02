package io.rotlabs.postmanandroidclient.ui.makeRequest.params

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.FragmentParamsHeadersBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.RequestConfigSharedViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.*
import javax.inject.Inject


class ParamsFragment : BaseFragment<FragmentParamsHeadersBinding, ParamsViewModel>(),
    View.OnClickListener, KeyValueIncludeChangeListener, KeyValueDeleteListener {

    @Inject
    lateinit var requestConfigSharedViewModel: RequestConfigSharedViewModel

    lateinit var addKeyValueAdapter: AddKeyValueAdapter

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
        setupParamsRecyclerView()
    }

    override fun setupObservables() {
        super.setupObservables()
        requestConfigSharedViewModel.paramList.observe(this, {
            Log.d("PUI", "keyValueList $it")
            addKeyValueAdapter.updateData(it)
        })
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

    private fun setupParamsRecyclerView() {
        binding.rvKeyValueConfigs.apply {
            // TODO fetch list from local store
            addKeyValueAdapter = AddKeyValueAdapter(
                this@ParamsFragment,
                arrayListOf(),
                this@ParamsFragment,
                this@ParamsFragment,
                childFragmentManager,
                KeyValueType.QUERY_PARAM
            )
            adapter = addKeyValueAdapter
            layoutManager =
                LinearLayoutManager(
                    this@ParamsFragment.requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
    }

    private fun showAddKeyValueBottomSheet() {
        val fragment = AddKeyValueBottomSheet.newInstance(KeyValueType.QUERY_PARAM, null)
        fragment.show(childFragmentManager, AddKeyValueBottomSheet.TAG)
    }

    override fun onKeyValueIncludeChange(keyValueConfig: KeyValueConfig, position: Int) {
        Log.d("PUI", "onKeyValueChange")

        addKeyValueAdapter.dataList[position] = keyValueConfig
        requestConfigSharedViewModel.paramList.postValue(addKeyValueAdapter.dataList)


    }

    override fun onDeleteKeyValueConfig(keyValueConfig: KeyValueConfig, position: Int) {
        addKeyValueAdapter.dataList.removeAt(position)
        addKeyValueAdapter.notifyDataSetChanged()
        requestConfigSharedViewModel.paramList.value?.remove(keyValueConfig)
        requestConfigSharedViewModel.paramList.postValue(requestConfigSharedViewModel.paramList.value)
    }


}