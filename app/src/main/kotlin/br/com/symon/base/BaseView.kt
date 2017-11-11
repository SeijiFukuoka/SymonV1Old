package br.com.symon.base

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String?)
}