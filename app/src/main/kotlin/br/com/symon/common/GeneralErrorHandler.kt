package br.com.gold360.financas.common

import br.com.symon.base.BaseView
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import java.lang.ref.WeakReference
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class GeneralErrorHandler(private val throwable: Throwable, view: BaseView? = null, private val onFailure: () -> Unit) : Throwable() {

    private val viewReference: WeakReference<BaseView> = WeakReference<BaseView>(view)

    companion object {
        const val UNKNOWN_ERROR = 0
    }

    init {
        call()
    }

    private fun call() {
        if (isNetworkError(throwable)) {
            showMessage(throwable.message)
        } else if (throwable is HttpException) {
            handleError(throwable)
        }

        onFailure()
    }

    private fun isNetworkError(throwable: Throwable?): Boolean {
        return throwable is SocketException ||
                throwable is UnknownHostException ||
                throwable is SocketTimeoutException
    }

    private fun handleError(throwable: HttpException?) {
        if (throwable?.code() != UNKNOWN_ERROR) {
            showMessage(throwable?.message)
        }
    }

    private fun showMessage(message: String?) {
        viewReference.get()?.showError(message)
    }
}