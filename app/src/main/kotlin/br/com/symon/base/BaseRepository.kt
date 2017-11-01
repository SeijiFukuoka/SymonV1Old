package br.com.symon.base

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class BaseRepository {
    fun <T> call(apiCall: Observable<T>) : Observable<T> {
        return apiCall.cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}