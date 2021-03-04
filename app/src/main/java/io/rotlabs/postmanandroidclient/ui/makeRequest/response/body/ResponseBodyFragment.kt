package io.rotlabs.postmanandroidclient.ui.makeRequest.response.body

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.databinding.FragmentResponseBodyBinding
import io.rotlabs.postmanandroidclient.utils.common.Toaster
import okio.Buffer


class ResponseBodyFragment : Fragment() {
    private lateinit var binding: FragmentResponseBodyBinding

    private lateinit var responseBodyText: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResponseBodyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPrettyBody.setOnClickListener {
            binding.btnRawBody.isChecked = false
            binding.btnPrettyBody.isChecked = true

            binding.tvResponseBodyText.text = indentJsonString(responseBodyText)
        }

        binding.btnRawBody.setOnClickListener {
            binding.btnRawBody.isChecked = true
            binding.btnPrettyBody.isChecked = false

            binding.tvResponseBodyText.text = responseBodyText
        }
        binding.btnPrettyBody.isChecked = true
        binding.tvResponseBodyText.text = indentJsonString(responseBodyText)

    }

    /**
     *  helper function  to set the response text to be displayed in this layout
     */
    fun setResponseText(responseText: String) {
        responseBodyText = responseText
    }

    /**
     *  to show the response in Pretty Print format
     */
    private fun indentJsonString(json: String): String {

        if (json.isEmpty()) {
            return json
        }
        return try {
            val source = Buffer().writeUtf8(json)
            val reader = JsonReader.of(source)
            val value = reader.readJsonValue()
            val adapter: JsonAdapter<Any> =
                Moshi.Builder().build().adapter(Any::class.java).indent("\t")
            adapter.toJson(value)
        } catch (e: Exception) {
            Toaster.show(requireContext(), "Unable to show pretty print")
            json
        }
    }

}