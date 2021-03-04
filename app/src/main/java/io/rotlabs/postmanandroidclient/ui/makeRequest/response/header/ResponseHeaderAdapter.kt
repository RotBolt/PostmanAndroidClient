package io.rotlabs.postmanandroidclient.ui.makeRequest.response.header

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.rotlabs.postmanandroidclient.databinding.ItemResponseHeaderBinding

class ResponseHeaderAdapter(private val dataList: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<ResponseHeaderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseHeaderViewHolder {
        return ResponseHeaderViewHolder(
            ItemResponseHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResponseHeaderViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun updateList(newList: ArrayList<Pair<String, String>>) {
        this.dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}