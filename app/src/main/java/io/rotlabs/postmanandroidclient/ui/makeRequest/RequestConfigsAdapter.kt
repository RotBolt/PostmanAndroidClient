package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.rotlabs.postmanandroidclient.ui.makeRequest.params.ParamsFragment

class RequestConfigsAdapter(makeRequestActivity: MakeRequestActivity) :
    FragmentStateAdapter(makeRequestActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ParamsFragment()
            1 -> ParamsFragment()
            2 -> ParamsFragment()
            3 -> ParamsFragment()
            else -> throw RuntimeException()
        }
    }

}