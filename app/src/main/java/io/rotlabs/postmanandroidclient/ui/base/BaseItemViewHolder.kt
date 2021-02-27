package io.rotlabs.postmanandroidclient.ui.base

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.di.component.DaggerViewHolderComponent
import io.rotlabs.postmanandroidclient.di.component.ViewHolderComponent
import io.rotlabs.postmanandroidclient.di.modules.ViewHolderModule
import io.rotlabs.postmanandroidclient.utils.common.Toaster
import javax.inject.Inject

/**
 * LifeCycle aware ViewHolder
 * This class provides convenient methods to handle its lifecycle
 *
 * If viewholder is in Paused state, data won't be updated in this viewholder
 *
 * Example:
 * If the activity goes in background, then viewholder's lifecycle also goes into Pause State
 * When activity enters Resume state, then viewHolder's lifecycle also goes into Resume state
 *
 * If view is outside of user window, then there is no need to update window, then it enters in Pause state
 */

abstract class BaseItemViewHolder<T : Any, VM : BaseItemViewModel<T>>(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    @Inject
    lateinit var lifecycleRegistry: LifecycleRegistry

    @Inject
    lateinit var viewModel: VM


    /**
     * Marks LifeCycle Created for this ViewHolder
     */
    fun onCreate() {
        injectDependencies(getViewHolderComponent())
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        setUpObservables()
        setupView(itemView)
    }

    /**
     * Marks LifeCycle Started -> Resumed for this ViewHolder
     * For Example : when viewholders are visible on screen then there is state is Resumed
     */
    fun onStart() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    /**
     * Marks LifeCycle Started -> Created for this ViewHolder
     * For Example : when viewholders are not visible on screen then there is state is Stopped
     */
    fun onStop() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    /**
     * Marks LifeCycle Destroyed for this ViewHolder
     */
    fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        viewModel.onManualCleared()
    }

    fun bind(data: T) {
        viewModel.updateData(data)
    }


    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    /**
     *  Inject dependencies to provide required objects needed by this ViewHolder
     *  Child classes need to call voewHolder.inject()
     *  to allow Dagger to provide required dependencies
     */
    protected abstract fun injectDependencies(buildComponent: ViewHolderComponent)

    /**
     *  Child classes need to extend this function in order to observe
     *  data from viewModel
     */
    protected abstract fun setupView(view: View)

    /**
     *  Builds ViewHolderComponent for ViewHolder that inject Dependencies
     */
    private fun getViewHolderComponent(): ViewHolderComponent {
        return DaggerViewHolderComponent.builder()
            .viewHolder(this)
            .viewHolderModule(ViewHolderModule())
            .appComponent((itemView.context.applicationContext as PostmanApp).getAppComponent())
            .build()
    }

    /**
     *  Child classes need to extend this function in order to observe
     *  data from viewModel
     */
    protected open fun setUpObservables() {
        viewModel.errorMessage.observe(this, {
            showToast(it)
        })
    }

    protected fun showToast(message: String) {
        Toaster.show(itemView.context, message)
    }
}