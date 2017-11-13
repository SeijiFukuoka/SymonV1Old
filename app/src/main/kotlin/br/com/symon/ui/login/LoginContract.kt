package br.com.symon.ui.login

import br.com.symon.base.BaseView
import br.com.symon.data.model.CheckUserResponse

class LoginContract {
    interface View : BaseView {
        fun handleCheckResponse(checkResponse: CheckUserResponse)
    }

    interface Presenter {
        fun checkUser(email: String)
    }
}