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
    private val dataList: ArrayList<KeyValueConfig>,
    private val keyValueAddRemoveListener: KeyValueAddRemoveListener,
    private val fragmentManager: FragmentManager
) :
    BaseAdapter<KeyValueConfig, AddKeyValueViewHolder>(parent.lifecycle, dataList),
    OnDeleteAdapterItemListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddKeyValueViewHolder {
        return AddKeyValueViewHolder(
            ItemKeyValueConfigBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            keyValueAddRemoveListener,
            this,
            fragmentManager
        )
    }

    override fun deleteItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }
}