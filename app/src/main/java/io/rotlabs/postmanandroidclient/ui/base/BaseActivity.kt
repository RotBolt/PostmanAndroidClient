package io.rotlabs.postmanandroidclient.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.di.component.ActivityComponent
import io.rotlabs.postmanandroidclient.di.component.DaggerActivityComponent
import io.rotlabs.postmanandroidclient.di.modules.ActivityModule
import io.rotlabs.postmanandroidclient.utils.common.Toaster
import javax.inject.Inject

abstract class BaseActivity<B : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: VM

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies(getActivityComponent())
        binding = provideActivityBinding()
        setContentView(binding.root)
        setupView(savedInstanceState)
        setupObservables()
        viewModel.onCreate()
    }

    /**
     *  View Binding for this activity.
     *  Child classes need to provide ViewBinding to setup the view
     */
    protected abstract fun provideActivityBinding(): B

    /**
     *  Inject dependencies to provide required objects needed by this Activity
     *  Child classes need to call activityComponent.inject()
     *  to allow Dagger to provide required dependencies
     */
    protected abstract fun injectDependencies(buildComponent: ActivityComponent)

    /**
     *  Builds ActivityComponent for activity that inject Dependencies
     */
    private fun getActivityComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
            .activity(this)
            .appComponent((application as PostmanApp).getAppComponent())
            .activityModule(ActivityModule())
            .build()
    }

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


    protected fun showToast(message: String) {
        Toaster.show(this, message)
    }

}