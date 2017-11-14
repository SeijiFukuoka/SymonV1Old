package br.com.symon.ui.login

import br.com.symon.base.BaseView
import br.com.symon.data.model.UserTokenRequest
import br.com.symon.data.model.UserTokenResponse

class LoginConfirmationContract {
    interface View : BaseView {
        fun handleTokenResponse(userTokenResponse: UserTokenResponse)
        fun handleUserNotFoundResponse()
    }

    interface Presenter {
        fun getUserToken(userTokenRequest: UserTokenRequest)
    }
}