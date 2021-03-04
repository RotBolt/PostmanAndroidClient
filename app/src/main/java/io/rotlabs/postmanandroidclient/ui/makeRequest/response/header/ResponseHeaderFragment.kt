package io.rotlabs.postmanandroidclient.ui.makeRequest.response.header

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.FragmentResponseHeaderBinding


class ResponseHeaderFragment : Fragment() {
    private lateinit var binding: FragmentResponseHeaderBinding

    private var responseHeaderMap: Map<String, String> = mapOf()

    private var responseHeaderAdapter: ResponseHeaderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResponseHeaderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvResponseHeaders.apply {
            val headerList = responseHeaderMap.map {
                it.key to it.value
            }.toList()

            responseHeaderAdapter =
                ResponseHeaderAdapter(headerList as ArrayList<Pair<String, String>>)

            adapter = responseHeaderAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    fun setHeaderMap(headers: Map<String, String>) {
        responseHeaderMap = headers
        val headerList = responseHeaderMap.map {
            it.key to it.value
        }.toList()


        responseHeaderAdapter?.updateList(headerList as ArrayList<Pair<String, String>>)
    }

}