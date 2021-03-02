package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.ItemKeyValueConfigBinding
import io.rotlabs.postmanandroidclient.di.component.ViewHolderComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseItemViewHolder
import io.rotlabs.postmanandroidclient.utils.common.OnDeleteAdapterItemListener

class AddKeyValueViewHolder(
    private val binding: ItemKeyValueConfigBinding,
    private val keyValueIncludeChangeListener: KeyValueIncludeChangeListener,
    private val keyValueDeleteListener: KeyValueDeleteListener,
    private val fragmentManager: FragmentManager,
    private val keyValueType: String
) : BaseItemViewHolder<KeyValueConfig, AddKeyValueItemViewModel>(binding) {

    init {

        binding.toIncludeCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.data.value?.let {
                it.toInclude = isChecked
                viewModel.updateData(it)
                keyValueIncludeChangeListener.onKeyValueIncludeChange(it, adapterPosition)
            }
        }
        binding.root.setOnClickListener {
            val fragment = AddKeyValueBottomSheet.newInstance(keyValueType, viewModel.data.value)
            fragment.show(fragmentManager, AddKeyValueBottomSheet.TAG)
        }

        binding.ivDelete.setOnClickListener {
            keyValueDeleteListener.onDeleteKeyValueConfig(viewModel.data.value!!, adapterPosition)
        }
    }

    override fun injectDependencies(buildComponent: ViewHolderComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(view: View) {
    }

    override fun setUpObservables() {
        super.setUpObservables()
        viewModel.data.observe(this, { keyValueConfig ->
            Log.d("PUI", "keyValueConfig $keyValueConfig")
            with(binding) {
                tvKey.text = keyValueConfig.key
                tvValue.text = keyValueConfig.value
                toIncludeCheckBox.isChecked = keyValueConfig.toInclude
            }
        })
    }


}