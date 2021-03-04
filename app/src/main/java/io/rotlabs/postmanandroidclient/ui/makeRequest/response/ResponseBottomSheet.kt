package io.rotlabs.postmanandroidclient.ui.makeRequest.response

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import io.rotlabs.postmanandroidclient.databinding.FragmentResponseBottomSheetBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseBottomSheetFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.MakeRequestSharedViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.response.body.ResponseBodyFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.response.header.ResponseHeaderFragment
import javax.inject.Inject


class ResponseBottomSheet :
    BaseBottomSheetFragment<FragmentResponseBottomSheetBinding, ResponseViewModel>() {


    companion object {
        const val TAG = "ResponseBottomSheet"
    }

    @Inject
    lateinit var makeRequestSharedViewModel: MakeRequestSharedViewModel

    private val responseInfoFragments =
        listOf<Fragment>(ResponseBodyFragment(), ResponseHeaderFragment())

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResponseBottomSheetBinding {
        return FragmentResponseBottomSheetBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupRequestInfoTabLayout(responseInfoFragments, this)
    }

    override fun setupObservables() {
        super.setupObservables()
        makeRequestSharedViewModel.requestProgress.observe(this, {
            binding.progressContainer.isVisible = it
        })

        makeRequestSharedViewModel.response.observe(this, { response ->
            response?.let {
                binding.noRequestResponseContainer.isVisible = false
                (responseInfoFragments[1] as ResponseHeaderFragment).setHeaderMap(it.headers.toMap())
            }
        })
        makeRequestSharedViewModel.responseText.observe(this, { responseString ->
            responseString?.let {
                binding.responseInfoContainer.isVisible = true
                (responseInfoFragments[0] as ResponseBodyFragment).setResponseText(it)
            }
        })
    }

    private fun setupRequestInfoTabLayout(fragmentList: List<Fragment>, parent: Fragment) {
        binding.responseInfoPager.adapter = ResponseInfoAdapter(fragmentList, parent)
        binding.responseInfoPager.isUserInputEnabled = false
        TabLayoutMediator(
            binding.responseInfoTabLayout,
            binding.responseInfoPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Body"
                1 -> tab.text = "Headers"
            }
        }.attach()
    }
}