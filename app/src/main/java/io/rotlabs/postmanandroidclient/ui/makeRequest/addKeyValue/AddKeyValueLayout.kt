package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import io.rotlabs.postmanandroidclient.PostmanApp
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.LayoutKeyValueConfigBinding
import javax.inject.Inject

/**
 *  Custom view to display Key Value configs required for making request
 *  In addition, view handles add , remove and update key value configs on its own
 *
 *  Parent view/ViewGroup have access to
 *  @property keyValueConfigDataHolder
 *  which they can observe and react according to changes
 */
class AddKeyValueLayout : FrameLayout, View.OnClickListener, KeyValueIncludeChangeListener,
    KeyValueDeleteListener {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    /**
     *  holds key Value configs added by user
     */
    @Inject
    lateinit var keyValueConfigDataHolder: KeyValueConfigDataHolder

    /**
     *  Refers to custom view's layout
     */
    private lateinit var rootLayoutBinding: LayoutKeyValueConfigBinding

    /**
     *   Refers to Parent view's lifecycle
     *   That is, Parent must be a lifecycle owner
     */
    private lateinit var lifecycleOwner: LifecycleOwner

    /**
     *  required to show AddKeyValueBottomSheet
     *  @see AddKeyValueBottomSheet
     */
    private lateinit var fragmentManager: FragmentManager

    /**
     *  holds the Type of Key Value Config
     *  @see KeyValueType
     */
    private lateinit var keyValueType: String

    private lateinit var addKeyValueAdapter: AddKeyValueAdapter

    private fun init() {
        injectDependencies()
        inflateKeyValueConfigLayout()
    }

    /**
     *  Inflate this view's layout and add to this view's container
     */
    private fun inflateKeyValueConfigLayout() {
        rootLayoutBinding =
            LayoutKeyValueConfigBinding.inflate(LayoutInflater.from(context), this, false)

        addView(rootLayoutBinding.root)
    }

    private fun injectDependencies() {
        (context.applicationContext as PostmanApp).getAppComponent().inject(this)
    }

    /**
     *  Set up the all clicksListeners, texts and functionality of this layout
     *  Parent view must call this method for this layout to work
     *
     *  @param keyValueType Type of Key Value Config to be added
     *  @see KeyValueType
     *
     *  @param parent Parent View/ViewGroup which contains this layout. Must be Lifecycle owner
     *
     *  @param fragmentManager required for showing AddKeyValueBottomSheet
     *  @see AddKeyValueBottomSheet
     *
     *  @param initialData List of KeyValueConfig need to be populated in this layout
     *  @see KeyValueConfig
     */

    fun loadLayout(
        keyValueType: String,
        parent: LifecycleOwner,
        fragmentManager: FragmentManager,
        initialData: ArrayList<KeyValueConfig> = arrayListOf()
    ) {
        lifecycleOwner = parent
        this.fragmentManager = fragmentManager
        this.keyValueType = keyValueType

        setupRecyclerView(initialData, lifecycleOwner, keyValueType, fragmentManager)
        setupBtnAddKeyValueConfig(keyValueType)
        setupObservables(keyValueType)

    }


    /**
     *  Observe the changes in KeyValueConfigDataHolder and update UI according to changes
     */
    private fun setupObservables(keyValueType: String) {
        if (keyValueType == KeyValueType.HEADER) {
            keyValueConfigDataHolder.headerList.observe(lifecycleOwner, {
                addKeyValueAdapter.updateData(it)
            })
        } else if (keyValueType == KeyValueType.QUERY_PARAM) {
            keyValueConfigDataHolder.paramList.observe(lifecycleOwner, {
                addKeyValueAdapter.updateData(it)
            })
        } else if (keyValueType == KeyValueType.FORM_DATA) {
            keyValueConfigDataHolder.formDataList.observe(lifecycleOwner, {
                addKeyValueAdapter.updateData(it)
            })
        }

    }

    private fun setupRecyclerView(
        dataList: ArrayList<KeyValueConfig>,
        parent: LifecycleOwner,
        keyValueType: String,
        fragmentManager: FragmentManager
    ) {
        addKeyValueAdapter =
            AddKeyValueAdapter(parent, dataList, this, this, fragmentManager, keyValueType)

        rootLayoutBinding.rvKeyValueConfigs.apply {
            adapter = addKeyValueAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun showAddKeyValueBottomSheet(fragmentManager: FragmentManager, keyValueType: String) {
        val fragment = AddKeyValueBottomSheet.newInstance(keyValueType, null)
        fragment.show(fragmentManager, AddKeyValueBottomSheet.TAG)
    }

    private fun setupBtnAddKeyValueConfig(keyValueType: String) {
        rootLayoutBinding.btnAddKeyValueConfig.text = "Add $keyValueType"
        rootLayoutBinding.btnAddKeyValueConfig.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddKeyValueConfig -> {
                showAddKeyValueBottomSheet(fragmentManager, keyValueType)
            }
        }
    }

    override fun onKeyValueIncludeChange(keyValueConfig: KeyValueConfig, position: Int) {
        addKeyValueAdapter.dataList[position] = keyValueConfig
        when (keyValueType) {
            KeyValueType.QUERY_PARAM -> {
                keyValueConfigDataHolder.paramList.postValue(addKeyValueAdapter.dataList)
            }
            KeyValueType.FORM_DATA -> {
                keyValueConfigDataHolder.formDataList.postValue(addKeyValueAdapter.dataList)
            }
            KeyValueType.HEADER -> {
                keyValueConfigDataHolder.headerList.postValue(addKeyValueAdapter.dataList)
            }
        }
    }

    override fun onDeleteKeyValueConfig(keyValueConfig: KeyValueConfig, position: Int) {
        addKeyValueAdapter.dataList.removeAt(position)
        addKeyValueAdapter.notifyDataSetChanged()
        keyValueConfigDataHolder.paramList.value?.remove(keyValueConfig)
        keyValueConfigDataHolder.paramList.postValue(keyValueConfigDataHolder.paramList.value)
    }


}