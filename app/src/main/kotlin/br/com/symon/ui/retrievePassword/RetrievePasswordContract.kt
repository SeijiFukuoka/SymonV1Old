package br.com.symon.ui.retrievePassword

import br.com.symon.base.BaseView


interface RetrievePasswordContract {
    interface View : BaseView {
        fun goToNextStep()
        fun showErrorMessage(message: String?)
    }

    interface Presenter {
        fun requestNewPassword(userEmail: String)
    }
}