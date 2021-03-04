package io.rotlabs.postmanandroidclient.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.di.component.DaggerFragmentComponent
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.di.modules.FragmentModule
import io.rotlabs.postmanandroidclient.utils.common.Toaster
import javax.inject.Inject

abstract class BaseBottomSheetFragment<B : ViewBinding, VM : BaseViewModel> :
    BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModel: VM

    protected lateinit var binding: B

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
        binding = initializeBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(savedInstanceState)
    }

    override fun getTheme(): Int = R.style.AppModalStyle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

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
     *  Builds FragmentComponent for fragment that inject Dependencies
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