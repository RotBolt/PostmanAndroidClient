package io.rotlabs.postmanandroidclient.utils.common

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider
import java.util.concurrent.TimeUnit

/**
 *  To follow  The Interface Segregation Principle (ISP) of SOLID principles
 */
fun EditText.registerTextChange(): Observable<String> {

    val subject = PublishSubject.create<String>()
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { subject.onNext(it.toString()) }
        }

        override fun afterTextChanged(s: Editable?) {}

    })

    setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
            subject.onComplete()
            return@OnEditorActionListener true
        }
        return@OnEditorActionListener false
    })

    return subject
}

fun Observable<String>.applySearchRxOpeations(schedulerProvider: SchedulerProvider): Observable<String> {
    return this
        // hold the stream and emit latest item after 300 Millis
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .filter { it.isNotEmpty() }
        // switch to new Observable and unsubscribe from previously generated stream
        // since that won't be needed
        .switchMap { Observable.just(it) }
        .subscribeOn(schedulerProvider.ui())
        .observeOn(schedulerProvider.ui())
}