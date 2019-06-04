package com.krcell.classkeeper.extentions

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.uber.autodispose.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.dispatchDefault(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.dispatchDefault(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


fun <T> Flowable<T>.dispatchDefault(): Flowable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.dispatchDefault(): Completable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


fun <T> Observable<T>.bindLifeCycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
    this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

fun <T> Single<T>.bindLifeCycle(owner: LifecycleOwner): SingleSubscribeProxy<T> =
    this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

fun <T> Flowable<T>.bindLifeCycle(owner: LifecycleOwner): FlowableSubscribeProxy<T> =
    this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

fun Completable.bindLifeCycle(owner: LifecycleOwner): CompletableSubscribeProxy =
    this.`as`(AutoDispose.autoDisposable<Any>(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))


private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }
private val onCompleteStub: () -> Unit = {}

fun <T : Any> ObservableSubscribeProxy<T>.subscribeBy(
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onNext: (T) -> Unit = onNextStub
): Disposable {
    return if (onError === onErrorStub && onComplete === onCompleteStub) {
        subscribe(onNext)
    } else {
        subscribe(onNext, onError, onComplete)
    }
}

fun <T : Any> SingleSubscribeProxy<T>.subscribeBy(
    onError: (Throwable) -> Unit = onErrorStub,
    onNext: (T) -> Unit = onNextStub
): Disposable {
    return if (onError === onErrorStub) {
        subscribe(onNext)
    } else {
        subscribe(onNext, onError)
    }
}

fun <T : Any> FlowableSubscribeProxy<T>.subscribeBy(
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub,
    onNext: (T) -> Unit = onNextStub
): Disposable {
    return if (onError === onErrorStub && onComplete === onCompleteStub) {
        subscribe(onNext)
    } else {
        subscribe(onNext, onError, onComplete)
    }
}

fun CompletableSubscribeProxy.subscribeBy(
    onError: (Throwable) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub
): Disposable {
    return subscribe(onComplete, onError)
}