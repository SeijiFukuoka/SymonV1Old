package br.com.gold360.financas.common

import android.support.annotation.StringRes
import br.com.symon.R
import br.com.symon.base.BaseView
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class GeneralErrorHandler(private val throwable: Throwable,
                          view: BaseView? = null,
                          private val onFailure: () -> Unit) : Throwable() {

    private val viewReference: WeakReference<BaseView> = WeakReference<BaseView>(view)

    companion object {
        const val UNKNOWN_ERROR = 0
    }

    init {
        call()
    }

    private fun call() {
        if (isNetworkError(throwable)) {
            showMessage(R.string.general_internet_connection_unavailable)
        } else if (throwable is HttpException) {
            handleError(throwable)
        }
        viewReference.get()?.hideLoading()
        onFailure()
    }

    private fun isNetworkError(throwable: Throwable?): Boolean {
        return throwable is SocketException ||
                throwable is UnknownHostException ||
                throwable is SocketTimeoutException ||
                throwable is ConnectException
    }

    private fun handleError(throwable: HttpException?) {
        if (throwable?.code() != UNKNOWN_ERROR) {
            showMessage(R.string.general_server_error)
        }
    }

    private fun showMessage(@StringRes strResId: Int) {
        viewReference.get()?.showError(strResId)
    }
}