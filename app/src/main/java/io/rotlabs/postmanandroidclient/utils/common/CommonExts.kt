package io.rotlabs.postmanandroidclient.utils.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 *  To follow  The Interface Segregation Principle (ISP) of SOLID principles
 */
fun EditText.registerTextChange(textChangeCallBack: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textChangeCallBack(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {}

    })
}