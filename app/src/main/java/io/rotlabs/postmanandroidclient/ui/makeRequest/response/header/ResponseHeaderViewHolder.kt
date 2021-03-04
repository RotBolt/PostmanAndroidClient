package io.rotlabs.postmanandroidclient.ui.makeRequest.response.header

import androidx.recyclerview.widget.RecyclerView
import io.rotlabs.postmanandroidclient.databinding.ItemResponseHeaderBinding

class ResponseHeaderViewHolder(private val binding: ItemResponseHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(keyValuePair: Pair<String, String>) {
        binding.tvResponseHeaderKey.text = keyValuePair.first
        binding.tvResponseHeaderValue.text = keyValuePair.second
    }
}