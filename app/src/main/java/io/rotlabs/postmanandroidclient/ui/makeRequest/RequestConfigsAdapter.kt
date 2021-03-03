package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.rotlabs.postmanandroidclient.ui.makeRequest.auth.AuthFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.body.BodyFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.header.HeaderFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.params.ParamsFragment

class RequestConfigsAdapter(makeRequestActivity: MakeRequestActivity) :
    FragmentStateAdapter(makeRequestActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ParamsFragment()
            1 -> AuthFragment()
            2 -> HeaderFragment()
            3 -> BodyFragment()
            else -> throw RuntimeException()
        }
    }

}