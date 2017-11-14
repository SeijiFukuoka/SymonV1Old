package br.com.symon.base

import android.support.annotation.StringRes

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(@StringRes message: Int)
}