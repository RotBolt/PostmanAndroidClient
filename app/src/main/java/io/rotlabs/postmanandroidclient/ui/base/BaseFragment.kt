package io.rotlabs.postmanandroidclient.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.di.component.DaggerFragmentComponent
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.di.modules.FragmentModule
import io.rotlabs.postmanandroidclient.utils.common.Toaster
import javax.inject.Inject

abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel> : Fragment() {


    @Inject
    lateinit var viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(getFragmentComponent())
        super.onCreate(savedInstanceState)
        setupObservables()
        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initializeBinding(inflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(savedInstanceState)
    }


    /**
     *  View Binding for this fragment
     *  Child classes need to provide ViewBinding to setup the view
     */
    protected abstract fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?): B

    /**
     *  Inject dependencies to provide required objects needed by this fragment
     *  Child classes need to call fragmentComponent.inject()
     *  to allow Dagger to provide required dependencies
     */
    protected abstract fun injectDependencies(buildComponent: FragmentComponent)

    /**
     *  Child classes need to implement this function to setup their activity or views
     *  setting up View.OnClickListeners, adapters goes in here
     */
    protected abstract fun setupView(savedInstanceState: Bundle?)

    /**
     *  Child classes need to extend this function in order to observe
     *  data from viewModel
     */
    protected open fun setupObservables() {
        viewModel.errorMessage.observe(this, {
            showToast(it)
        })
    }

    /**
     *  Builds ActivityComponent for activity that inject Dependencies
     */
    private fun getFragmentComponent(): FragmentComponent {
        return DaggerFragmentComponent.builder()
            .fragment(this)
            .fragmentModule(FragmentModule())
            .appComponent((requireActivity().application as PostmanApp).getAppComponent())
            .build()
    }

    protected fun showToast(message: String) {
        Toaster.show(requireContext(), message)
    }
}