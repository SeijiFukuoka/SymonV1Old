package br.com.symon.ui.welcome

import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.UserFacebookAuthenticateRequest
import br.com.symon.data.model.responses.RegisterUserResponse
import br.com.symon.data.model.responses.UserTokenResponse


interface WelcomeContract {
    interface View : BaseView {
        fun handleTokenResponse(userTokenResponse: UserTokenResponse?)
        fun handleUserNotFoundResponse()
        fun goToNextStep(registerUserResponse: RegisterUserResponse?)
        fun showErrorMessage(message: String?)
    }

    interface Presenter {
        fun getUserToken(userAuthenticateRequest: UserFacebookAuthenticateRequest)
        fun register(userAuthenticateRequest: UserFacebookAuthenticateRequest)
    }
}