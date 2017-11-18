package br.com.symon.ui.register

import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.UserAuthenticateRequest


interface RegisterContract {
    interface View: BaseView {
        fun goToNextStep(id: Int?)
        fun showErrorMessage(message: String?)
    }

    interface Presenter {
        fun register(userAuthenticateRequest: UserAuthenticateRequest)
    }
}