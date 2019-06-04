package com.krcell.classkeeper.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposes = CompositeDisposable()

    override fun onCleared() {
        disposes.clear()
        super.onCleared()
    }

    protected fun Disposable.addDispose() {
        disposes.add(this)
    }
}