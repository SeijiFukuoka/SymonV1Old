package br.com.symon.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


open class BaseRepository {
    fun <T> call(apiCall: Observable<T>): Observable<T> {
        return apiCall.cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}