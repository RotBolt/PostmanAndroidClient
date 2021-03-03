package io.rotlabs.postmanandroidclient.ui.makeRequest.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import io.reactivex.disposables.CompositeDisposable
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.data.models.AuthType
import io.rotlabs.postmanandroidclient.data.models.BodyInfo
import io.rotlabs.postmanandroidclient.data.models.BodyType
import io.rotlabs.postmanandroidclient.data.models.FormDataContent
import io.rotlabs.postmanandroidclient.databinding.FragmentBodyBinding
import io.rotlabs.postmanandroidclient.di.component.FragmentComponent
import io.rotlabs.postmanandroidclient.ui.base.BaseFragment
import io.rotlabs.postmanandroidclient.ui.makeRequest.MakeRequestSharedViewModel
import io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue.KeyValueType
import io.rotlabs.postmanandroidclient.utils.common.applySearchRxOpeations
import io.rotlabs.postmanandroidclient.utils.common.hideSoftKeyboard
import io.rotlabs.postmanandroidclient.utils.common.registerTextChange
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import java.util.*
import javax.inject.Inject

class BodyFragment : BaseFragment<FragmentBodyBinding, BodyViewModel>(),
    AdapterView.OnItemSelectedListener {


    @Inject
    lateinit var makeRequestSharedViewModel: MakeRequestSharedViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private var currentBodyInfo: BodyInfo = BodyInfo.NoBody()

    private val currentFormDataMap = mutableMapOf<String, FormDataContent>()

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBodyBinding {
        return FragmentBodyBinding.inflate(layoutInflater, container, false)
    }

    override fun injectDependencies(buildComponent: FragmentComponent) {
        buildComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupBodyTypeSpinner()
        setupRawBodyContentBox()
        binding.addKeyValueLayout.loadLayout(KeyValueType.FORM_DATA, this, childFragmentManager)
    }

    override fun setupObservables() {
        super.setupObservables()
        binding.addKeyValueLayout.keyValueConfigDataHolder.formDataList.observe(this, { list ->
            if (currentBodyInfo is BodyInfo.FormDataBody) {
                currentFormDataMap.clear()
                list.forEach {
                    if (it.toInclude) {
                        currentFormDataMap[it.key] = FormDataContent.FormDataText(it.value)
                    }
                }
                (currentBodyInfo as BodyInfo.FormDataBody).content = currentFormDataMap
                makeRequestSharedViewModel.bodyInfo.postValue(currentBodyInfo)
            }
        })
    }

    private fun setupBodyTypeSpinner() {
        val bodyTypes: Array<String> = EnumSet
            .allOf(BodyType::class.java)
            .map { it.name }
            .toTypedArray()

        val bodyTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bodyTypes)
        bodyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.bodyTypeSpinner.adapter = bodyTypeAdapter
        binding.bodyTypeSpinner.onItemSelectedListener = this
        binding.bodyTypeSpinner.setSelection(0)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.bodyTypeSpinner.setSelection(position)
        when (position) {
            0 -> showAndSelectNoBody()
            1 -> showAndSelectFormDataBody()
            2 -> showAndSelectRawBody()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    private fun showAndSelectNoBody() {
        with(binding) {
            tvNoBody.isVisible = true
            addKeyValueLayout.isVisible = false
            etRawBodyBox.isVisible = false
        }

        currentBodyInfo = BodyInfo.NoBody()
        makeRequestSharedViewModel.bodyInfo.postValue(currentBodyInfo)
    }

    private fun showAndSelectFormDataBody() {
        with(binding) {
            tvNoBody.isVisible = false
            addKeyValueLayout.isVisible = true
            etRawBodyBox.isVisible = false
        }

        currentBodyInfo = BodyInfo.FormDataBody(currentFormDataMap)
        makeRequestSharedViewModel.bodyInfo.postValue(currentBodyInfo)
    }

    private fun showAndSelectRawBody() {
        with(binding) {
            tvNoBody.isVisible = false
            addKeyValueLayout.isVisible = false
            etRawBodyBox.isVisible = true
        }

        val rawContent = binding.etRawBodyBox.text?.toString() ?: ""
        currentBodyInfo = BodyInfo.RawBody(rawContent)
        makeRequestSharedViewModel.bodyInfo.postValue(currentBodyInfo)
    }

    private fun setupRawBodyContentBox() {
        compositeDisposable.add(
            binding.etRawBodyBox.registerTextChange()
                .applySearchRxOpeations(schedulerProvider)
                .subscribe({
                    (currentBodyInfo as BodyInfo.RawBody).content = it
                    makeRequestSharedViewModel.bodyInfo.postValue(currentBodyInfo)
                }, {
                    showToast(it.message ?: "Oops")
                }, {
                    requireActivity().hideSoftKeyboard()
                    binding.etRawBodyBox.clearFocus()
                })
        )
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
}