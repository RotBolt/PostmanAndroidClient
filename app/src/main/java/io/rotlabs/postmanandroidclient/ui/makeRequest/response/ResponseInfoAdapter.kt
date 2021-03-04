package io.rotlabs.postmanandroidclient.ui.makeRequest.response

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ResponseInfoAdapter(private val fragmentList: List<Fragment>, parent: Fragment) :
    FragmentStateAdapter(parent) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}