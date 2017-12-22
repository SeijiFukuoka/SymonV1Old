package br.com.symon.ui.register

import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.UserAuthenticateRequest
import br.com.symon.data.model.responses.RegisterUserResponse


interface RegisterContract {
    interface View: BaseView {
        fun goToNextStep(registerUserResponse: RegisterUserResponse?)
        fun showErrorMessage(message: String?)
    }

    interface Presenter {
        fun register(userAuthenticateRequest: UserAuthenticateRequest)
    }
}