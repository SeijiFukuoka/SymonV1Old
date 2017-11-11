package br.com.symon.ui.login

import br.com.symon.base.BaseView
import br.com.symon.data.model.User

class LoginContract {
    interface View : BaseView {
        fun showLoginResponse(user: User)
    }

    interface Presenter {
        fun login(user: User)
    }
}