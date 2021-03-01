package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.view.View
import androidx.fragment.app.FragmentManager
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.ItemKeyValueConfigBinding
import io.rotlabs.postmanandroidclient.di.component.ViewHolderComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseItemViewHolder
import io.rotlabs.postmanandroidclient.utils.common.OnDeleteAdapterItemListener

class AddKeyValueViewHolder(
    private val binding: ItemKeyValueConfigBinding,
    private val keyValueAddRemoveListener: KeyValueAddRemoveListener,
    private val deleteAdapterItemListener: OnDeleteAdapterItemListener,
    private val fragmentManager: FragmentManager
) : BaseItemViewHolder<KeyValueConfig, AddKeyValueViewModel>(binding),
    KeyValueAddRemoveListener {
    override fun injectDependencies(buildComponent: ViewHolderComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(view: View) {
        binding.toIncludeCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.data.value?.let {
                it.toInclude = isChecked
                viewModel.updateData(it)
                keyValueAddRemoveListener.addOrRemoveKeyValue(
                    it.key,
                    it.value,
                    it.description,
                    isChecked
                )
            }
        }
        binding.ivEdit.setOnClickListener {
            val fragment = AddKeyValueBottomSheet.newInstance(false)
            fragment.setKeyValueAddRemoveListener(this)
            fragment.show(fragmentManager, AddKeyValueBottomSheet.TAG)
        }
    }

    override fun setUpObservables() {
        super.setUpObservables()
        viewModel.data.observe(this, { keyValueConfig ->
            with(binding) {
                tvKey.text = keyValueConfig.key
                tvValue.text = keyValueConfig.value
                toIncludeCheckBox.isChecked = keyValueConfig.toInclude
            }
        })
    }

    override fun addOrRemoveKeyValue(
        key: String,
        value: String,
        description: String,
        toAdd: Boolean
    ) {
        with(viewModel) {

            data.value?.let {
                it.key = key
                it.value = value
                it.description = description
                it.toInclude = toAdd
                updateData(it)
            }

            keyValueAddRemoveListener.addOrRemoveKeyValue(key, value, description, toAdd)
        }
    }

}