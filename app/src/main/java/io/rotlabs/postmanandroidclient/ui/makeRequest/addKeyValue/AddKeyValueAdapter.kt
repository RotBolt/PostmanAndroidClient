package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import io.rotlabs.postmanandroidclient.databinding.ItemKeyValueConfigBinding
import io.rotlabs.postmanandroidclient.ui.base.BaseAdapter
import io.rotlabs.postmanandroidclient.utils.common.OnDeleteAdapterItemListener

class AddKeyValueAdapter(
    parent: LifecycleOwner,
    val dataList: ArrayList<KeyValueConfig>,
    private val keyValueIncludeChangeListener: KeyValueIncludeChangeListener,
    private val keyValueDeleteListener: KeyValueDeleteListener,
    private val fragmentManager: FragmentManager,
    private val keyValueType: String
) :
    BaseAdapter<KeyValueConfig, AddKeyValueViewHolder>(parent.lifecycle, dataList) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddKeyValueViewHolder {
        return AddKeyValueViewHolder(
            ItemKeyValueConfigBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            keyValueIncludeChangeListener,
            keyValueDeleteListener,
            fragmentManager,
            keyValueType
        )
    }

}